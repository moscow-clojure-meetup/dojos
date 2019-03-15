'use strict';

const http = require('http');
const path = require('path');
const express = require('express');
const dotenv = require('dotenv');
const morgan = require('morgan');
const ejs = require('ejs');

dotenv.config();

const index = path.resolve('public/index.ejs');
const port = process.env.PORT || 8080;
const config = {
    env: process.env.NODE_ENV,
    firebase: {
        apiKey: process.env.FIREBASE_API_KEY,
        authDomain: process.env.FIREBASE_AUTH_DOMAIN,
        databaseURL: process.env.FIREBASE_DATABASE_URL,
        projectId: process.env.FIREBASE_PROJECT_ID
    }
};

const app = express();
app.engine('html', ejs.renderFile);

app.use(express.static('public', {index: false}));
app.use(morgan('combined'));

app.use(function (req, res) {
    res.render(index, {config: config});
});

http
  .createServer(app)
  .listen(port, () => console.log('Http server started'));
