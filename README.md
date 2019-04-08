<h1>JsonLangEditor 0.1</h1>

This is a simple JavaFX app to make process of getting Angular web app translations from CSV to json faster.

<h2>Features:</h2>
<h3>Import CSV file</h3>
*  The first 4 columns (maximum) should contain keys and translations)
*  You can remove lines with DELETE key or by pressingg "remove selected lines" button. Multiselect is supported.

![image1](https://imgur.com/35xqqkI.png)

<h3>Merge</h3>
* On the left, there is textarea where you can paste your json file with translations.
* Autosort by keys
* JSON validation. Text turns red when JSOn is invalid.
* On the right you can choose which columns from the imported CSV you want to use. Key values should always be on left.
* Merge button adds selected lines to json. 
* Merge all button adds all lines.
* Duplicate keys are not allowed, values in json are replaced by values from CSV.
* Lines with missing keys are ignored.

![image2](https://i.imgur.com/p9XvaBi.png)

App creates "_settings.data" file to remember last window size and position.