Threat : The Paper uses tools like Junit factory,Evosuite and Randoop to generate the automated Unit test cases.
These tools were selected because the base paper of the paper under discussion was also using the same tools. 
Which is not appropriate representation of technological advancements in the same sector rather technological advancement of the tool.


Traces:
Construct validity 
The paper is a reconstruction of its underlying paper and compares the old tools with their newer version for checking the growth , which means that the advancements of tools are a measure of comparision. 
Where as the actual growth of the field is not measured by the growth of the tools but rather the improvement in the technology for those particular tools.


Theory:
As The Base paper is comparing the tools growth to measure the growth in the field of automation unit testing.It is actually a measure of growth of the tools and not the growth of the field.
This Threat has been adressed by me in my reproduction effort by taking into consideration a new tool which is AI based "Diffblue". 
The tool was compatible with the code and was able to generate Unit test cases and with the increased number of test cases we could see that there has been some improvement in the area.
To further analyse this we now group the test cases generated (both manually and automated) under their repective categorise to see if the automation tools have actually surpassed the manual test case generation.
Furthermore a comparison between how the tools approch have changed over time has been compared to see the devlopment in the field.


Feasibility:
As an indepth analysis would mean reading all the generated test cases and classifying them under categorieswhich would mean going through 100 test case per class with a total of 20 classes.
Due to time limitations i have restricted the comparision to 3 classes which would mean comparing roughly 300 test cases and also presenting the improvement in the field.


Implimentation:
The comparision between the types of test cases generated gives us a clearer overview of exactly which kind of testing can be acheived using automated test.As we already know from the assingment 2 that the coverage has improved.
From our experiment, the next step is to analyze what changes have been made in the field , to get an overview of how the tools have changed.Which is then compared to the official metrice on the website of the tool itself to eastablish a conclusion.


Results:
We see that the field of test Automation has evolved to incorporate various other fields like:
1. AI
2. Machine learning
3. Deep learning

How AI/ML ehances test case automation? 
1. AI-powered Visual Validation tools
   The key objective of image-based visual validation is to ensure that the UI appears correctly to the user. Which could not be done with older unit test software's. ML-powered visual validation tools help to automate these since ML can recognize patterns.[1]
2. API testing: 
   Machine learning algorithm boil down the testing to just backend which makes it easier to test any piece of code. [1]
3. Automated running of test cases depending on code change
   AI/ML algorithms have superior analytics capabilities; therefore, you can find out what is the minimum set of test cases you should run to test a small change to the code. [1]
4. Creating test cases.
    AI/ML algorithms can “read” your application and learn about it. These algorithms build a data set that contains observations about your application, including how its various features should behave given certain conditions. [1]

Relation to the experiment:
The application Diffblue is an AI based automated test generation tool which was successfully able to perform below tasks which could not be performed in the earlier versions of test automation tools.
1. Creation of Negative scenarios
2. Creation of black box scenarios
3. Creation of Boundary Value scenarios.
4. Creation of positive scenarios.

The second page in the observed data excelsheet covers all the function declared in three classes and then looks out for the kinds of test cases defined in both approaches i.e. manually and Automated using DiffBlue.
These test cases are grouped under six categories Namely:

1. Negative testing :		Types of errors covered: ArrayIndexOutOfBoundsException,NumberFormatException,NegativeArraySizeException,IndexOutOfBoundsException,IllegalArgumentException
2. Black Box testing:		When the test  is examining the behaviour of the functionby feeding values in it and not looking at what is happeneing inside the function
3. Boundary Value Analysis:	When a test is feeding  values on the edges of valid input also including invalid inputs
4. White Box testing:		When The test is looking at the functionality of a function when feeding values to it.
5. Functional test:		When the functional behaviour of the peice of code is being checked by feeding appropriate input.
6. Positive test:		When the behaviour of a peice of code is observed by feeding the known positive values.

 
After Grouping the test cases generated automated and manually (Page two of observed results excelsheet) we see:
1. Automation tools are unable to provide functional test cases for a given peice of code.
2. Better code coverage is provided by automation tools
3. Better test coverage is provided by automation tools.
4. Automation tools treat the code as a black box and check for outcomes
5. With a perfectly defined code with pre defined assertions automated unit test tools can provide a good coverage.


Comparison between Randoop, evosuite and Diffblue(cover):
The table  has been taken from the official website of diffblue(cover) [3].
![Diffblue comparison](https://d1qmdf3vop2l07.cloudfront.net/strong-whale.cloudvent.net/hash-store/f856bc1ea6d92c751ac7d8287e042851.png)

Comparing the results achieved by our experiment and the results given on the website we can conclude that our experiment results correspond to the behavior which has been reported on the official website.
Which is : “AI based tools not only create a higher number of test cases but also a higher quality of test cases which have a better coverage,
and Automated testing might have a good coverage but to acheive a perfect test structure  manual and automation testing both are needed as the functional and white box tests can not be performed with the automation tools."



Requirements:
Same as assingment two as the experiment was performed in assingment 2



Process: 
Same as assingment two as the experiment was performed in assingment 2, the categorisation guidelines for test cases  have been provided in the excel sheet on the same page.



Data:
The data used in the Assingment 3 is a derivative of results from assingment 2 and hence the newly generated data is placed in data folder which includes the comparision and grouping of test cases.

References:
[1] https://testsigma.com/blog/can-ai-driven-test-automation-enhance-test-automation/
[2] https://www.parasoft.com/what-is-artificial-intelligence-in-software-testing/
[3] https://www.diffblue.com/blog/java/testing/java-unit-test-generator-comparison-diffblue-cover-vs-evosuite-vs-randoop-vs-squaretest/
