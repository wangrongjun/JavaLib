<!doctype html>
<html lang="en">

	<head>
		<meta charset="UTF-8" />
		<title>${course.courseName}</title>
	</head>

	<body>
		<center>
			<h1>${course.courseName}</h1>
		</center>

		<p>课程学习提示：</p>
		<p>${course.courseHint}</p>

		<h2>章节列表</h2>
		<hr>
		<#list course.chapterNames?sort as chapterName>
			${chapterName_index}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>${chapterName}</span>
			<br>
		</#list>

		<h2>视频列表</h2>
		<hr>
		<#list course.videos?sort_by( "title") as video>
			${video_index}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="${video.realUrl}">${video.title}</a>
			<br>
		</#list>

		<h2>课程资料列表</h2>
		<hr>
		<#list course.courseDataFiles?sort_by( "name") as courseDataFile>
			${courseDataFile_index}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="${courseDataFile.url}">${courseDataFile.name}</a>
			<br>
		</#list>
	</body>

</html>