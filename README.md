**Ripple Search**

**Steps 1-4 implemented**

1. There is an SQL table with column ID and column text

2. Search a string in text and return IDs if match found 

Expression: Text contains <String> 

Search an expression in text  

Expression: 

<String> OR <String> 

<String> AND <String> 

(<String> OR <String>) AND (<String> OR <String>) 

(<String> AND <String>) OR (<String> OR <String>) 

3. Ignore special characters & only search for alpha numeric. Made search case-insensitive.

4. Ignore/remove noise words

**TO DO**
* Update to use Stanford Stop Word Removal Annotator & do not remove "and" or "or"
* Does SQL query need to search all tables?

5. Lemmatization and stemming 

6. Performance (Indexing, distributed computing)