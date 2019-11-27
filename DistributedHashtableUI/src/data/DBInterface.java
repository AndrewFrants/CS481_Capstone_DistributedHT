package data;

import java.util.*;
import java.text.*; // for date and time
import java.net.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

public class DBInterface {
	static AmazonDynamoDB dynamoDB;

	private static void init() throws Exception {
		/*
		 * To configure the Credentials value including the "access_key_id" and
		 * "secret_key_id" which could also be found in ./.aws/credentials
		 */
		// change the below values to match with your instance

		BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAUIHPUI4YTCAA7B7P", "oZSIhlggA1KcWd53SzK9/5udZiH7HTgjwfZWjiH1");
		dynamoDB = AmazonDynamoDBClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.withRegion("us-east-2") // the region of your instance --- the availability zone showed in your EC2									// main page
				.build();

	}

	public static void insertDocument(String title, String file, String tableName) throws Exception {
		init();
		try {
			System.out.println("uploading " + title + " to table " + tableName + " on DynamoDB");
			// String tableName = tableName;
			// Create a table with a primary hash key named 'name', which holds a string
			CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
					.withKeySchema(new KeySchemaElement().withAttributeName("Title").withKeyType(KeyType.HASH))
					.withAttributeDefinitions(new AttributeDefinition().withAttributeName("Title")
							.withAttributeType(ScalarAttributeType.S))
					.withProvisionedThroughput(
							new ProvisionedThroughput().withReadCapacityUnits(3L).withWriteCapacityUnits(3L));

			// Create table if it does not exist yet
	
			TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
			// wait for the table to move into ACTIVE state
			TableUtils.waitUntilActive(dynamoDB, tableName);
			System.out.println("uploading " + title + " to table " + tableName + " on DynamoDB");
			// Add an item
			Map<String, AttributeValue> item;
			item = newItem(title, file);
			PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
			PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to AWS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with AWS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}

	private static Map<String, AttributeValue> newItem(String title, String file) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("Title", new AttributeValue(title));
		item.put("File", new AttributeValue(file));
		return item;

	}

	public static ArrayList<Document> getDocumentList() throws Exception {
		init();
		ArrayList<Document> docList = new ArrayList<Document>();
		ScanRequest scanRequest = new ScanRequest().withTableName("Documents");
		
	

		ScanResult scanResult = dynamoDB.scan(scanRequest);
		System.out.println("test");
		for (Map<String, AttributeValue> item : scanResult.getItems()) {
			docList.add(getItem(item));
		}

		return docList;

	}

	private static Document getItem(Map<String, AttributeValue> attributeList) {
			 int index = 0;
			 Document doc = new Document("");
			 for (Map.Entry<String, AttributeValue> item : attributeList.entrySet()) {
		            String attributeName = item.getKey();
		            AttributeValue value = item.getValue();
		           if(index ==0) {
		        	  doc.setTitle(value.getS());
		           }
		         index++;
		         
		         
			
		 }	
 return doc;


}
}