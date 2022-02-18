/*
 * Copyright (c) 2021, Valaphee.
 * All rights reserved.
 */

const HtmlWebpackPlugin = require('html-webpack-plugin');
const TerserPlugin = require('terser-webpack-plugin');

module.exports = {
  mode: 'production',
  entry: './src/main/webapp/index.tsx',
  output: {
    filename: '[name].[chunkhash:4].js',
    publicPath: '',
  },
  module: {
    rules: [{
      test: /\.(svg|png|jpg|jpeg|gif)$/,
      use: {
        loader: 'file-loader',
        options: {
          name: '[name].[ext]',
          outputPath: 'static',
        },
      },
    }, {
      test: /\.s[ac]ss$/i,
      use: [
        'style-loader',
        'css-loader',
        {
          loader: 'sass-loader',
          options: {
            implementation: require('sass'),
          },
        },
      ],
    }, {
      test: /\.(ts|tsx)$/,
      exclude: /node_modules/,
      use: 'ts-loader',
    }],
  },
  resolve: {
    extensions: ['.js', '.jsx', '.ts', '.tsx'],
  },
  optimization: {
    minimizer: [new TerserPlugin({
      parallel: true,
    })],
  },
  plugins: [new HtmlWebpackPlugin({
    template: 'src/main/webapp/index.html',
    filename: '../templates/index.html',
  })],
  externals: {
    config: 'config',
  },
};
