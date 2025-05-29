#!/usr/bin/env node

// JSON Minifier Script
const fs = require('fs');
if (process.argv.length < 3) {
    const scriptName = process.argv[1];
    console.error(`Usage: node ${scriptName} <filename>`);
    process.exit(1);
}

const filename = process.argv[2];
if (!fs.existsSync(filename)) {
    console.error(`File not found: ${filename}`);
    process.exit(1);
}

const fileContent = fs.readFileSync(filename, 'utf8');
const json = JSON.parse(fileContent);

const minified = JSON.stringify(json);
const outputFilename = filename.replace(/\.json$/, '.min.json');
fs.writeFileSync(outputFilename, minified, 'utf8');
console.log(`Minified JSON written to: ${outputFilename}`);
