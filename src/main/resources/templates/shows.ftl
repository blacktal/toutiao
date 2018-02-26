<html>
<body>
<pre>
show animals and human.
<#--canyouseeme?-->
Animal is ${animal}
Animal is $!{}

<#--遍历List-->
<#list colors as color>
    ${color}
</#list>

<#--遍历Map-->
<#--方法一：直接遍历map-->
<#list map?keys as key>
    key:${key} - value:${map[key]}
</#list>

<#--方法二：取出map的keys集合-->
<#assign keys=map?keys/>
<#list keys as key>
    key:${key} - value:${map[key]}
</#list>

<#--方法三：直接取map的value值集合-->
<#list map?values as value>
    ${value}
</#list>

<#--取对象的属性-->
${newUser.name}
<#--assign name=newUser?getName/-->
<#--${name}-->

<#assign title = "nowcoder"/>
<#include "/news.ftl"/>

<#import "/header.ftl" as header1/>
aka ${header1.aka}

<#macro greet color index>
Hello by macro ${index}, ${color}
</#macro>
<#list colors as color>
<@greet color = color index = color_index/>
</#list>

<#assign hello = "hello"/>
<#assign hworld1 = "${hello!} world"/>
<#assign hworld2 = '${hello!} world'/>

hworld1:${hworld1!}
hworld2:${hworld2!}


</pre>
</body>
</html>