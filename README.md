# Data Ingestion

Two Approach:
1) In-memroy Buffer
2) Using Kafka

**Note: I have not implemented the 2nd approach. I have shared design on my thought process. Also, due to time constraint I did not add test case. I will add when I find time**

## In-memory Buffer:

This approaches uses an one in-memory data store in each instance. If the in-memory buffer hits the memory threshold or age of first records exceeds the time threshold, content will be uploaded to S3. All the API req in a server will be adding data to one in-memory buffer in a synchronized fashion and the upload to s3 is triggered in async manner. All the memory threshold and age threshold limits can be configured via properties. 

<img width="1370" height="394" alt="Screenshot 2025-07-14 at 3 15 31 PM" src="https://github.com/user-attachments/assets/ec780643-0a29-4c0a-98ec-a613956229f2" />


### Aws Infra Setup:
I have setup this whole infra in us-east-1a region. VPC names is "saravanakumar-vpc". I have created two public and private subnet. Added route table to the subnet and added IG to public subnet. I have deployed the app as container in the ec2 instance. ALB is not created as it is demo app. App can be access directly with ec2 public dns name.

AWS url : http://ec2-3-88-142-52.compute-1.amazonaws.com/  <br>
S3 bucket : [saravanakumar_portal](https://us-east-1.console.aws.amazon.com/s3/buckets/saravanakumar-portal26?region=us-east-1&bucketType=general&tab=properties) <br>
Directory Structure : yyyy/mm/dd/<server_ip_address>_HH:MM:SS <br>


## Using Kafka
In the second approach, kafka comsumer parameters can be tuned to immitate the near realtime ingestion behaviour. As soon as API request is received, the data will be published in to kafka broker. Consumers will fetch and batch the S3 operation.<br> 

Consumer Parameters to tune are:
1) Max no of bytes fetched in a partition.
2) Max no of records to fetch by enabling batching.
3) Time to wait between each consumer poll from the broker.

Here, we have additional cost factor of kafka resource.

<img width="1042" height="312" alt="Screenshot 2025-07-14 at 5 32 54 PM" src="https://github.com/user-attachments/assets/9550813d-4436-4060-8b5a-174aacc51ddd" />








