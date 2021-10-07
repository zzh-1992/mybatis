# [Association](https://mybatis.org/mybatis-3/sqlmap-xml.html#)

```xml
<association property="author" javaType="Author">
<id property="id" column="author_id"/>
<result property="username" column="author_username"/>
</association>
```

The association element deals with a "has-one" type relationship. For example, in our example, a Blog has one Author.
An association mapping works mostly like any other result. You specify the target property, 
the javaType of the property (which MyBatis can figure out most of the time), the jdbcType if necessary 
and a typeHandler if you want to override the retrieval of the result values.

Where the association differs is that you need to tell MyBatis how to load the association. 
MyBatis can do so in two different ways:

* Nested Select: By executing another mapped SQL statement that returns the complex type desired.
* Nested Results: By using nested result mappings to deal with repeating subsets of joined results.
First, let's examine the properties of the element. As you'll see, it differs from a normal result mapping 
  only by the select and resultMap attributes.

Attribute |	Description
---|---
property|	The field or property to map the column result to. If a matching JavaBeans property exists 
for the given name, then that will be used. Otherwise, MyBatis will look for a field of the given name. 
In both cases you can use complex property navigation using the usual dot notation. For example, 
you can map to something simple like: username, or to something more complicated like: address.street.number.
javaType|	A fully qualified Java class name, or a type alias (see the table above 
for the list of built- in type aliases). MyBatis can usually figure out the type if you're mapping to a JavaBean. 
However, if you are mapping to a HashMap, then you should specify the javaType explicitly to ensure the desired behaviour.
jdbcType|	The JDBC Type from the list of supported types that follows this table. 
The JDBC type is only required for nullable columns upon insert, update or delete. 
This is a JDBC requirement, not an MyBatis one. So even if you were coding JDBC directly, 
you'd need to specify this type – but only for nullable values.
typeHandler	We discussed default type handlers previously in this documentation. 
Using this property you can override the default type handler on a mapping-by-mapping basis. 
The value is either a fully qualified class name of a TypeHandler implementation, or a type alias.

## Nested _Select_ for Association

Attribute |    Description
---- |---- 
column |    The column name from the database, or the aliased column label that holds the value that will be passed 
to the nested statement as an input parameter. This is the same string that would normally be passed 
to resultSet.getString(columnName). Note: To deal with composite keys, you can specify multiple column names 
to pass to the nested select statement by using the syntax column="{prop1=col1,prop2=col2}". 
This will cause prop1 and prop2 to be set against the parameter object for the target nested select statement.
select |    The ID of another mapped statement that will load the complex type required by this property mapping. 
The values retrieved from columns specified in the column attribute will be passed 
to the target select statement as parameters. A detailed example follows this table. 
Note: To deal with composite keys, you can specify multiple column names to pass to the nested 
select statement by using the syntax column="{prop1=col1,prop2=col2}". This will cause prop1 and prop2 
to be set against the parameter object for the target nested select statement.
fetchType |    Optional. Valid values are lazy and eager. 
If present, it supersedes the global configuration parameter lazyLoadingEnabled for this mapping.

For example:
```xml
<resultMap id="blogResult" type="Blog">
  <association property="author" column="author_id" javaType="Author" select="selectAuthor"/>
</resultMap>

<select id="selectBlog" resultMap="blogResult">
  SELECT * FROM BLOG WHERE ID = #{id}
</select>

<select id="selectAuthor" resultType="Author">
  SELECT * FROM AUTHOR WHERE ID = #{id}
</select>
```

That's it. We have two select statements: one to load the Blog, the other to load the Author, 
and the Blog's resultMap describes that the selectAuthor statement should be used to load its author property.

All other properties will be loaded automatically assuming their column and property names match.

While this approach is simple, it will not perform well for large data sets or lists. 
This problem is known as the "N+1 Selects Problem". In a nutshell, the N+1 selects problem is caused like this:

* You execute a single SQL statement to retrieve a list of records (the "+1").
* For each record returned, you execute a select statement to load details for each (the "N").
This problem could result in hundreds or thousands of SQL statements to be executed. This is not always desirable.

The upside is that MyBatis can lazy load such queries, thus you might be spared the cost of these statements 
all at once. However, if you load such a list and then immediately iterate through it to access the nested data, 
you will invoke all of the lazy loads, and thus performance could be very bad.

And so, there is another way.

## _Nested Results_ for Association
Attribute |	Description 
---|---
resultMap |	This is the ID of a ResultMap that can map the nested results of this association into an appropriate object graph. This is an alternative to using a call to another select statement. It allows you to join multiple tables together into a single ResultSet. Such a ResultSet will contain duplicated, repeating groups of data that needs to be decomposed and mapped properly to a nested object graph. To facilitate this, MyBatis lets you "chain" result maps together, to deal with the nested results. An example will be far easier to follow, and one follows this table.
columnPrefix |	When joining multiple tables, you would have to use column alias to avoid duplicated column names in the ResultSet. Specifying columnPrefix allows you to map such columns to an external resultMap. Please see the example explained later in this section.
notNullColumn |	By default a child object is created only if at least one of the columns mapped to the child's properties is non null. With this attribute you can change this behaviour by specifiying which columns must have a value so MyBatis will create a child object only if any of those columns is not null. Multiple column names can be specified using a comma as a separator. Default value: unset.
autoMapping |	If present, MyBatis will enable or disable automapping when mapping the result to this property. This attribute overrides the global autoMappingBehavior. Note that it has no effect on an external resultMap, so it is pointless to use it with select or resultMap attribute. Default value: unset.
You've already seen a very complicated example of nested associations above. The following is a far simpler example to demonstrate how this works. Instead of executing a separate statement, we'll join the Blog and Author tables together, like so:

```xml
<select id="selectBlog" resultMap="blogResult">
  select
    B.id            as blog_id,
    B.title         as blog_title,
    B.author_id     as blog_author_id,
    A.id            as author_id,
    A.username      as author_username,
    A.password      as author_password,
    A.email         as author_email,
    A.bio           as author_bio
  from Blog B left outer join Author A on B.author_id = A.id
  where B.id = #{id}
</select>
```    
Notice the join, as well as the care taken to ensure that all results are aliased with a unique and clear name.
This makes mapping far easier. Now we can map the results:
```xml

<resultMap id="blogResult" type="Blog">
  <id property="id" column="blog_id" />
  <result property="title" column="blog_title"/>
  <association property="author" resultMap="authorResult" />
</resultMap>

<resultMap id="authorResult" type="Author">
  <id property="id" column="author_id"/>
  <result property="username" column="author_username"/>
  <result property="password" column="author_password"/>
  <result property="email" column="author_email"/>
  <result property="bio" column="author_bio"/>
</resultMap>
```

In the example above you can see at the Blog's "author" association delegates to the "authorResult" resultMap 
to load the Author instance.

Very Important: id elements play a very important role in Nested Result mapping. You should always specify one 
or more properties that can be used to uniquely identify the results. The truth is that MyBatis will still work 
if you leave it out, but at a severe performance cost. Choose as few properties as possible 
that can uniquely identify the result. The primary key is an obvious choice (even if composite).

Now, the above example used an external resultMap element to map the association. 
This makes the Author resultMap reusable. However, if you have no need to reuse it, or if you simply prefer 
to co-locate your result mappings into a single descriptive resultMap, you can nest the association result mappings. 
Here's the same example using this approach:

```xml
<resultMap id="blogResult" type="Blog">
  <id property="id" column="blog_id" />
  <result property="title" column="blog_title"/>
  <association property="author" javaType="Author">
    <id property="id" column="author_id"/>
    <result property="username" column="author_username"/>
    <result property="password" column="author_password"/>
    <result property="email" column="author_email"/>
    <result property="bio" column="author_bio"/>
  </association>
</resultMap>
```    
What if the blog has a co-author? The select statement would look like:
```xml
<select id="selectBlog" resultMap="blogResult">
  select
    B.id            as blog_id,
    B.title         as blog_title,
    A.id            as author_id,
    A.username      as author_username,
    A.password      as author_password,
    A.email         as author_email,
    A.bio           as author_bio,
    CA.id           as co_author_id,
    CA.username     as co_author_username,
    CA.password     as co_author_password,
    CA.email        as co_author_email,
    CA.bio          as co_author_bio
  from Blog B
  left outer join Author A on B.author_id = A.id
  left outer join Author CA on B.co_author_id = CA.id
  where B.id = #{id}
</select>
```
Recall that the resultMap for Author is defined as follows.
```xml
<resultMap id="authorResult" type="Author">
  <id property="id" column="author_id"/>
  <result property="username" column="author_username"/>
  <result property="password" column="author_password"/>
  <result property="email" column="author_email"/>
  <result property="bio" column="author_bio"/>
</resultMap>
```
Because the column names in the results differ from the columns defined in the resultMap, 
you need to specify columnPrefix to reuse the resultMap for mapping co-author results.
```xml
<resultMap id="blogResult" type="Blog">
    <id property="id" column="blog_id" />
    <result property="title" column="blog_title"/>
    <association property="author"
                 resultMap="authorResult" />
    <association property="coAuthor"
                 resultMap="authorResult"
                 columnPrefix="co_" />
</resultMap>
```

## _Multiple ResultSets_ for Association
Attribute |	Description
---|---
column|	When using multiple resultset this attribute specifies the columns (separated by commas) that will be correlated with the foreignColumn to identify the parent and the child of a relationship.
foreignColumn|	Identifies the name of the columns that contains the foreign keys which values will be matched against the values of the columns specified in the column attibute of the parent type.
resultSet|	Identifies the name of the result set where this complex type will be loaded from.Starting from version 3.2.3 MyBatis provides yet another way to solve the N+1 problem.Some databases allow stored procedures to return more than one resultset or execute more than one statement at once 
and return a resultset per each one. This can be used to hit the database just once and return related data
without using a join.

In the example, the stored procedure executes the following queries and returns two result sets. 
The first will contain Blogs and the second Authors.
```xml
SELECT * FROM BLOG WHERE ID = #{id}

SELECT * FROM AUTHOR WHERE ID = #{id}
```
A name must be given to each result set by adding a resultSets attribute 
to the mapped statement with a list of names separated by commas.
```xml
<select id="selectBlog" resultSets="blogs,authors" resultMap="blogResult" statementType="CALLABLE">
  {call getBlogsAndAuthors(#{id,jdbcType=INTEGER,mode=IN})}
</select>
```
Now we can specify that the data to fill the "author" association comes in the "authors" result set:
```xml
<resultMap id="blogResult" type="Blog">
  <id property="id" column="id" />
  <result property="title" column="title"/>
  <association property="author" javaType="Author" resultSet="authors" column="author_id" foreignColumn="id">
    <id property="id" column="id"/>
    <result property="username" column="username"/>
    <result property="password" column="password"/>
    <result property="email" column="email"/>
    <result property="bio" column="bio"/>
  </association>
</resultMap>
```
You've seen above how to deal with a "has one" type association. But what about "has many"? 
That's the subject of the next section.

