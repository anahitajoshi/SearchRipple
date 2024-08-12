**Code Walkthrough**


**class SearchImplementation**

Implements search functionality -- 
1. Takes in inputted search term, preprocesses and processes term for lowercase/uppercase, special characters, noise words, complex expressions, then SQL query created -- IDs must return any case variation & where text partially matches inputted search 
2. SQL query executed
3. IDs of terms contained search returned

**EXAMPLE INPUTTED SEARCH and SQL QUERY:**

search: one or _($seven and %eigh't)_

sql query: _SELECT ID FROM table WHERE LOWER(Text) LIKE LOWER('%one%') OR LOWER(Text) LIKE LOWER('%seven%') AND LOWER(Text) LIKE LOWER('%eight%')_


Functions:
**public List<Integer> search(String term)**

Input: search term inputted by user, could include special characters, noise words, uppercase/lowercase at this point

Main function to implement search functionality
1. Calls to preprocess inputted search term
2. Calls to query() function to tokenize and process new term to create SQL query
3. With the returned SQL query, execute the query using the java SQL package -- created a PreparedStatement and creating a ResultSet
4. ResultSet is used to create a list of integers

Output: List<Integer> containing IDs corresponding to text found that AT least matches inputted search and is case insensitive

**String query(String term)**

Input: search term inputted by user which has been preprocessed in search() function to remove special characters and noise words

Starts  SQL query, handles parentheses and tokenization
1. Adds spaces around parantheses so that parantheses can also be tokenized
2. Creates tokens from string
3. Calls to process() function to then create the SQL query, then returns results of that call

Output: Results of process() function which is a String that represents SQL query to execute

**String process(String[] tokens, int start, int end)**

Input: array of tokens created by query() function and start and end indices

Processes tokens and parentheses
1. Creates string of StringBuilder class to start SQL query
1. Processes through all tokens -- search terms, and, or, as well as parentheses
2. Finds corresponding opening and closing parentheses, then recursively processes tokens inside to handle complexx expressions
3. Creates  and returns SQL query that is case insensitive and has partial search (IDs of text will be returned if it contains AT LEAST search terms)

Output: String of SQL query with required functinoality

**int indexClosingParen(String[] tokens, int openingIndex)**

Input: array of tokens and the index of the opening parenthesis

1. Iterates tokens to find the corresponding closing parenthesis through nesting

Output: Index of closing parenthesis

**String preprocessSpecialCharNoiseWords(String term)**

Input: string of inputted search

Removes special characters and noise words
1. Stanford CoreNLP - tokenizes input and removes speicial characters
2. Removes noise words - leaving and & or?
3. Returns preprocessed input string without any special characters or noise words

Output: String of inputted search term without special characters or noise words