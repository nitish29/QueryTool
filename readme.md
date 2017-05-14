Data Store Query Tool Demo
===================
This repository contains the code for a **Query Tool** built using Java. The tool is used to import data into a data store and to perform subsequent queries. 

----------

Execution 
------------
This tool uses an external apache lirary, [Commons CLI], which provides an API for parsing command line options passed to programs.

I have created an executable **jar** file, **comScore.jar** that accepts CLI arguments to perform import and query operations. 

Before we can use the tool to perform query operation, we need to create a data store. This tool accepts text file with a **.txt** extension. It then parses this file line-by-line to create a file under a **datastore** directory with file name as **stbid_date_title.txt**, where the **stbid**, **date** and **title** are unique for each file. Each line parse is used to generate this new file and store data in it. While importing, if a file already exists with the same **stbid**, **date** and **title**, the contents of that file will be overwritten. 

Command to import the text file is:
```sh
    $ cd <path_to_executable_jar_file>
    $ java -jar comScore.jar -i path_to_the_text_file
```

Further operations supported by the tool are:
- **Select:** Select operation is used to select the attributes that need to be displayed when querying the data store. 
    - It displays the results based on the attributes provided following the `-s` argument  and only displays attributes that are requested when making a query. If `-s` option is not provided, all the attributes pertaining to a record are displayed.

        ```sh
        $ java -jar comScore.jar -s TITLE,REV,DATE 
        
        the matrix,4.00,2014-04-01
        unbreakable,6.00,2014-04-03
        the hobbit,8.00,2014-04-02
        the matrix,4.00,2014-04-02
        ```
        
        ```sh
        $ java -jar comScore.jar  
        
        stb1,the matrix,warner bros,2014-04-01,4.00,1:30
        stb1,unbreakable,buena vista,2014-04-03,6.00,2:05
        stb2,the hobbit,warner bros,2014-04-02,8.00,2:45
        stb3,the matrix,warner bros,2014-04-02,4.00,2:05
        ```

- **Filter:** Filter operation is used to perform record filtering based on single/multiple attributes provided by the user. The filter argument is expressed as a `key` => `value` pair.  

    - We use `-f` argument to specify the filters.
    
        ```sh
        $ java -jar comScore.jar -f DATE=2014-04-02
        
        stb2,the hobbit,warner bros,2014-04-02,8.00,2:45
        stb3,the matrix,warner bros,2014-04-02,4.00,2:05
        ```
        
        ```sh
        $ java -jar comScore.jar -f DATE=2014-04-02,STB=stb2
        
        stb2,the hobbit,warner bros,2014-04-02,8.00,2:45
        ```
        
        ```sh
        $ java -jar comScore.jar -f TITLE-"the matrix"
        
        stb1,the matrix,warner bros,2014-04-01,4.00,1:30
        stb3,the matrix,warner bros,2014-04-02,4.00,2:05
        ```
            
- **Order:** Order operation is used to perform ordering of records filtering based on single/multiple attributes provided by the user. The ordering is performed `right to left` i.e. from last order attribute to first order attribute provided. 
    - We use `-o` argument to specify the ordering.
    
        ```sh
        $  java -jar comScore.jar -o TITLE,STB 
        
        stb2,the hobbit,warner bros,2014-04-02,8.00,2:45
        stb1,the matrix,warner bros,2014-04-01,4.00,1:30
        stb3,the matrix,warner bros,2014-04-02,4.00,2:05
        stb1,unbreakable,buena vista,2014-04-03,6.00,2:05
        ```
   
- **Combined Operations**: All these aforementioned operations can be combined with each other to retrieve more detailed records. 

    ```sh
    $  java -jar comScore.jar -s TITLE,STB -f DATE=2014-04-02 -o REV
    
    the matrix,stb3
    the hobbit,stb2
    ```


> **Note:** If filter and order arugments are provided while making the query, first filtering would be performed and then ordering of records would take place.


***Operations in brief:***
| Request Type | CLI Operator | Example Query |
|---------|----------------|--------|
| `Import` | `-i`      | ```$ java -jar comScore.jar -i /a/b/c/test_file.txt```|
| `Select` | `-s`      | ```$ java -jar comScore.jar -s TITLE,REV,DATE```|
| `Filter`| `-f` | ```$ java -jar comScore.jar -f DATE=2014-04-02```|
| `Order` | `-o` | ```$ java -jar comScore.jar -o DATE,TITLE```|


***Supported Attributes:***
| Attribute | Description | 
|---------|----------------|
| `STB` | `The set top box id on which the media asset was viewed. (Text, max size 64 char)`| 
| `TITLE` | `TThe title of the media asset. (Text, max size 64 char)`| 
| `PROVIDER` | `The distributor of the media asset. (Text, max size 64 char)`| 
| `DATE` | `The local date on which the content was leased by through the STB (A date in YYYY-MM-DD format)`| 
| `REV` | `The price incurred by the STB to lease the asset. (Price in US dollars and cents)`| 
| `VIEW_TIME` | `The amount of time the STB played the asset.  (Time in hours:minutes)`| 

----


[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

[Commons CLI]: <https://commons.apache.org/proper/commons-cli/>
   

