# [collection](https://mybatis.org/mybatis-3/sqlmap-xml.html#) 

```xml

<collection property="posts" ofType="domain.blog.Post">
    <id property="id" column="post_id"/>
    <result property="subject" column="post_subject"/>
    <result property="body" column="post_body"/>
</collection>
```

The collection element works almost identically to the association. In fact, it's so similar, to document the
similarities would be redundant. So let's focus on the differences.

To continue with our example above, a Blog only had one Author. But a Blog has many Posts. On the blog class, this would
be represented by something like:

```java
private List<Post> posts;
```

To map a set of nested results to a List like this, we use the collection element. Just like the association element, we
can use a nested select, or nested results from a join.

## Nested Select for Collection

First, let's look at using a nested select to load the Posts for the Blog.

```xml

<resultMap id="blogResult" type="Blog">
    <collection property="posts" javaType="ArrayList" column="id" ofType="Post" select="selectPostsForBlog"/>
</resultMap>

<select id="selectBlog" resultMap="blogResult">
SELECT * FROM BLOG WHERE ID = #{id}
</select>

<select id="selectPostsForBlog" resultType="Post">
SELECT * FROM POST WHERE BLOG_ID = #{id}
</select>
```

There are a number things you'll notice immediately, but for the most part it looks very similar to the association
element we learned about above. First, you'll notice that we're using the collection element. Then you'll notice that
there's a new "ofType" attribute. This attribute is necessary to distinguish between the JavaBean (or field) property
type and the type that the collection contains. So you could read the following mapping like this:

```xml

<collection property="posts" javaType="ArrayList" column="id" ofType="Post" select="selectPostsForBlog"/>
```

Read as: "A collection of posts in an ArrayList of type Post."

The javaType attribute is really unnecessary, as MyBatis will figure this out for you in most cases. So you can often
shorten this down to simply:

```xml

<collection property="posts" column="id" ofType="Post" select="selectPostsForBlog"/>
```

## Nested Results for Collection
By this point, you can probably guess how nested results for a collection will work, because it's exactly the same as an association, but with the same addition of the ofType attribute applied.

First, let's look at the SQL:
```xml
<select id="selectBlog" resultMap="blogResult">
  select
  B.id as blog_id,
  B.title as blog_title,
  B.author_id as blog_author_id,
  P.id as post_id,
  P.subject as post_subject,
  P.body as post_body,
  from Blog B
  left outer join Post P on B.id = P.blog_id
  where B.id = #{id}
</select>
```
Again, we've joined the Blog and Post tables, and have taken care to ensure quality result column labels for simple mapping. Now mapping a Blog with its collection of Post mappings is as simple as:
```xml
<resultMap id="blogResult" type="Blog">
  <id property="id" column="blog_id" />
  <result property="title" column="blog_title"/>
  <collection property="posts" ofType="Post">
    <id property="id" column="post_id"/>
    <result property="subject" column="post_subject"/>
    <result property="body" column="post_body"/>
  </collection>
</resultMap>
```
Again, remember the importance of the id elements here, or read the association section above if you haven't already.

Also, if you prefer the longer form that allows for more reusability of your result maps, you can use the following alternative mapping:
```xml
<resultMap id="blogResult" type="Blog">
  <id property="id" column="blog_id" />
  <result property="title" column="blog_title"/>
  <collection property="posts" ofType="Post" resultMap="blogPostResult" columnPrefix="post_"/>
</resultMap>

<resultMap id="blogPostResult" type="Post">
  <id property="id" column="id"/>
  <result property="subject" column="subject"/>
  <result property="body" column="body"/>
</resultMap>
```

## Multiple ResultSets for Collection
As we did for the association, we can call a stored procedure that executes two queries and returns two result sets, one with Blogs and another with Posts:
```xml
SELECT * FROM BLOG WHERE ID = #{id}

SELECT * FROM POST WHERE BLOG_ID = #{id}
```
A name must be given to each result set by adding a resultSets attribute to the mapped statement with a list of names separated by commas.
```xml
<select id="selectBlog" resultSets="blogs,posts" resultMap="blogResult">
  {call getBlogsAndPosts(#{id,jdbcType=INTEGER,mode=IN})}
</select>
```
We specify that the "posts" collection will be filled out of data contained in the result set named "posts":
```xml
<resultMap id="blogResult" type="Blog">
  <id property="id" column="id" />
  <result property="title" column="title"/>
  <collection property="posts" ofType="Post" resultSet="posts" column="id" foreignColumn="blog_id">
    <id property="id" column="id"/>
    <result property="subject" column="subject"/>
    <result property="body" column="body"/>
  </collection>
</resultMap>
```

NOTE There's no limit to the depth, breadth or combinations of the associations and collections that you map. You should keep performance in mind when mapping them. Unit testing and performance testing of your application goes a long way toward discovering the best approach for your application. The nice thing is that MyBatis lets you change your mind later, with very little (if any) impact to your code.

Advanced association and collection mapping is a deep subject. Documentation can only get you so far. With a little practice, it will all become clear very quickly.






