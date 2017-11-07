## 101软件开发工程师（JAVA）部分题库-1

[TOC]

### 单选题

<#list rowList as row>
<#if row.type=="单选题">
###### ${row_index+1}.【${row.type}】${row.question}

    A. ${row.selectA}
    B. ${row.selectB}
    C. ${row.selectC}
    D. ${row.selectD}

> 答案：**${row.answer}**

</#if>
</#list>

### 多选题

<#list rowList as row>
<#if row.type=="多选题">
###### ${row_index+1}.【${row.type}】${row.question}

    A. ${row.selectA}
    B. ${row.selectB}
    C. ${row.selectC}
    D. ${row.selectD}

> 答案：**${row.answer}**

</#if>
</#list>

### 判断题

<#list rowList as row>
<#if row.type=="判断题">
###### ${row_index+1}.【${row.type}】${row.question}

> 答案：**<#if row.answer=="1">正确<#else>错误</#if>**

</#if>
</#list>

### 填空题

<#list rowList as row>
<#if row.type=="填空题">
###### ${row_index+1}.【${row.type}】${row.question}

> 答案：**${row.answer}**

</#if>
</#list>

### 问答题

<#list rowList as row>
<#if row.type=="问答题">
###### ${row_index+1}.【${row.type}】${row.question}

> 答案：

${row.answer}

</#if>
</#list>
