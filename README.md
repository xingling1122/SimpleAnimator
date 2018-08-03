# SimpleAnimator
Better use of Android Animation,SimpleAnimator can make you easier to use Animator than
native android api of animator.

How to use SimpleAnimator

gradle:

Step 1. Add repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.xingling1122:SimpleAnimator:1.1'
	}
  
 maven:
 
 Step 1. Add repository to your build file
 
 Add it in your root build.gradle at the end of repositories:

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  
Step 2. Add the dependency

	<dependency>
	    <groupId>com.github.xingling1122</groupId>
	    <artifactId>SimpleAnimator</artifactId>
	    <version>1.1</version>
	</dependency>
