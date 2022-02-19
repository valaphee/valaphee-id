/*
 * Copyright (c) 2021-2022, Valaphee.
 * All rights reserved.
 */

package com.valaphee.id.util

import org.passay.CharacterRule
import org.passay.EnglishCharacterData
import org.passay.EnglishSequenceData
import org.passay.IllegalSequenceRule
import org.passay.LengthRule
import org.passay.PasswordData
import org.passay.PasswordValidator
import org.passay.PropertiesMessageResolver
import org.passay.WhitespaceRule
import org.springframework.core.io.ClassPathResource
import java.util.Properties
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * @author Kevin Ludwig
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.TYPE, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [com.valaphee.id.util.PasswordValidator::class])
annotation class Password(
    val message: String = "Password is invalid.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

/**
 * @author Kevin Ludwig
 */
class PasswordValidator : ConstraintValidator<Password, String> {
    private lateinit var validator: PasswordValidator

    override fun initialize(constraintAnnotation: Password) {
        validator = PasswordValidator(
            PropertiesMessageResolver(Properties().apply { ClassPathResource("passay.properties").inputStream.use(::load) }), listOf(
                LengthRule(8, 64),
                CharacterRule(EnglishCharacterData.UpperCase, 1),
                CharacterRule(EnglishCharacterData.LowerCase, 1),
                CharacterRule(EnglishCharacterData.Digit, 1),
                CharacterRule(EnglishCharacterData.Special, 1),
                WhitespaceRule(),
                IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
                IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false)
            )
        )
    }

    override fun isValid(value: String, context: ConstraintValidatorContext): Boolean {
        val result = validator.validate(PasswordData(value))
        if (result.isValid) return true
        context.buildConstraintViolationWithTemplate(validator.getMessages(result).joinToString(",")).addConstraintViolation().disableDefaultConstraintViolation()
        return false
    }
}
