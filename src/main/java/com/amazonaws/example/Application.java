package com.amazonaws.example;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class Application {

	public static void main(String[] args) throws Exception {
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		try {
			credentialsProvider.getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(e);
		}
		AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().withCredentials(credentialsProvider)
				.withRegion(Regions.US_WEST_2).build();
		PersonCreateTable.createTable(dynamoDB);
		AddressCreateTable.createTable(dynamoDB);
		PersonContactCreateTable.createTable(dynamoDB);
	}
}
