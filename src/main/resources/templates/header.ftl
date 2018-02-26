<#assign aka = "niuke"/>

<#--这里不能插入父页面的变量，因为父页面引用header.ftl是用import变量，
它自己开辟了一个命名空间，并不是跟include一样简单地插入原命名空间-->
<#--Title <h>${title}</h>-->
Title <h>${title!}</h>