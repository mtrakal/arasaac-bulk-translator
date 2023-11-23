# [Arasaac](https://arasaac.org/) Bulk Translator

Project to help translators of [Arasaac pictograms](https://arasaac.org/pictograms/search) to different languages using Google Translate website (or other).

## How it works

- Download all the pictograms from Arasaac website
- Prepare docx file for upload to Google Translate website
- Download the translated docx file and replace the original one
- Login to Arasaac admin website with open Developer Tools and save your Bearer token
- Copy the token into app
- App will bulk upload all new translated keywords to Arasaac website (not modifying existing keywords or validated pictograms)

## How to use

- Install Java 17
- Download the latest release
- Run the app with `java -jar arasaac-bulk-translator-1.0.0.jar`
- Follow the instructions
