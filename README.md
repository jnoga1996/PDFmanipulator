# PDFmanipulator
Simple spring boot web app which allows user to merge two PDF files into one

## How to run

1. Open PowerShell and type ```git clone -b main --single-branch https://github.com/jnoga1996/PDFmanipulator```
2. After the repository is cloned type ```cd PDFmanipulator```
3. Run maven build by typing ```./mvnw clean install```
4. Run app by typing ```./mvnw spring-boot:run```
5. Paste following url into your browser:
http://localhost:8080/merge/
   
## How it works
1. Upload first .PDF file (File should be smaller than 10 MB)
2. Upload second .PDF file (File should be smaller than 10 MB)
3. Click Submit
4. Your files should be merged and available for download.
5. After few minutes scheduler will call file removing job, which will remove your files, so your data will not be stored on server.