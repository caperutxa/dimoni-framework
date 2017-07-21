# dimoni-framework


How to manage the regression testing is the idea behind that project.
Once the system grows, and the number of test with it, one need some way
to filter that test and run only what the system needs each time.

## Test list
Everithing rest on a list of tests with that format (# as separation character):
* id (int)
* test case name (string)
* technology used
* parameters
* components, tags or triggers

## Technology
The idea is to be independent of the tool or technology used.
Only one requirement. The test could be called via shell command

## framework and environment properties
* Mail functionality
* Test list (source and location)
* Technology (tech name and location)
* custom 

*CUSTOM* means that when you have a list of tests, each line is a test with some parameters
The system will try to "translate" all parameters to the correct value

## Sample
#### Test list
* 1#Test case#soapui#-s "Test suite" -Pendpoint=${web_service} -PuserId=10 -PsecondEnd=${front_url} ${soapui_loc}/project.xml#front,backend
* 2#Test case 2#selenium#hardcoded command line(not importnat now)#front

#### Properties
soapui=/home/user/soapui/runner.sh

soapui_loc=/home/user/soaupui/tests

live_web_service=http://live.service.com

live_front_url=http://front.com

test_web_service=http://test.service.comte

test_front_url=http://test.myfronttest.com

#### Command
* framework.jar -component=front -environment=test
* * will trigger the soapui file defined with that tests (and parameters)
* * 1#Test case#soapui#-s "Test suite" -Pendpoint=http://test.service.comte -PuserId=10 -PsecondEnd=http://test.myfronttest.com /home/user/soaupui/tests/project.xml#front,backend
* * 2#Test case 2#selenium#hardcoded command line(not importnat now)#front
* framework.jar -component=backend -environment=live
* * Will trigger only one test (that match with the component backend
* * 1#Test case#soapui#-s "Test suite" -Pendpoint=http://live.service.com -PuserId=10 -PsecondEnd=http://front.com /home/user/soaupui/tests/project.xml#front,backend

