<h1>JsonLangEditor 0.2</h1>

This is a simple JavaFX app to make process of getting Angular web app translations from CSV to json faster.

<h2>Features:</h2>
<h3>Import CSV file</h3>
*  The first 4 columns (maximum) should contain keys and translations. <br>
*  You can remove lines with DELETE key or by pressing "remove selected lines" button. Multiselect is supported.<br>

![image1](https://imgur.com/PY6mBVS.png)

<h3>Merge</h3>
* On the left, there is textarea where you can paste your json file with translations.<br>
* Option to choose if keys should be sorted <br>
* JSON validation. Text turns red when JSOn is invalid.<br>
* On the right you can choose which columns from the imported CSV you want to use. Key values should always be on left.<br>
* Merge button adds selected lines to json. <br>
* Merge all button adds all lines.<br>
* Duplicate keys are not allowed, values in json are replaced by values from CSV.<br>
* Lines with missing keys are ignored.<br>

![image2](https://imgur.com/4MTcacd.png)

App creates "_settings.data" file to remember last window size and position.

<h3>Download executable JAR</h3>
[OneDrive](https://1drv.ms/f/s!AvYEqSfRGivGh5RWDNNrp9LEKrX3Lg)

<br>
Requirements:<br>
Java Runtime Environment: 
https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
