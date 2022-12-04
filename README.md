# Reference Documentation

### Folder structure: 
 In the `.\src\main\java\org\sinhala\summarization`
 * AbstractSummary.java --> Generate Abstract summury
 * ConvertToSinhala.java --> Convert unicodes to Sinhala
 * DownloadPDF.java --> Download the Gazette PDF from website
 * ExtractSummary.java --> Generate Extractive summary
 * GenerateSummary.java --> Finilized the summary
 * KeyWords.java --> All the keywords
 *  ListDownFiles.java --> Check the previous downloaded PDFs (for testing bulk of PDFs)
 * PDFFormatter.java --> Format the PDF and remove unwanted lines
 * Server.java --> Main class (Execute this class to start the server)
 * ServiceController.java --> URL resources mapping
 * Utils.java --> All common Methods
 
### Buid
 * Execute the `mvn clean install` command under the root pom and it will generate the "sinhala.summary.generator-1.0.1.jar" in the `target` folder

		
### Application:
 * Go to the .\target and execute the "sinhala.summary.generator-1.0.1.jar" jar file with the following command
	  ```java -jar sinhala.summary.generator-1.0.1.jar```
 * Access the application through http://localhost:8080/ 
 * Copy the URL of any sinhala gazette PDF from http://documents.gov.lk/en/exgazette.php (Gazettes needs to have single page)
 * Insert the URL and click "Generate the summarization"
 * Application will give you the final output in the next window
	
### GIT location
 * https://github.com/rahalJayawardane/Summarization