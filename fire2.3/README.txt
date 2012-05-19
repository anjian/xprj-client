#####################################
##           Fire v2.3             ## 
##     Release Date: 21-01-2010    ##
#####################################

Description: 
------------------
Fire is a lightweight themable GUI engine for j2me MIDP2 applications. It is designed to be an eye-candy replacement to the traditional midp GUI components. It provides much more functionality than the midp standard components and its not kvm-implementation depended like the midp standard components. 

Since v2.0 Fire also has an xHTML module that allows the creation of mobile browsers and GUIs based on xHTML. Fire also comes with a set of utility classes for logging, internationalization and more. 

Midlets build with Fire has been succesfully tested with handrends of Devices including all major brands with j2me support like Blackberry and Nokia.

License: 
--------------- 
Fire-j2me is licensed under the LGPL. This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details. 

Copyright (C) 2006,2007,2008,2009,2010 Pashalis Padeleris (padeler at users.sourceforge.net)


History: 
----------------- 
Fire initially started as a toy project to see if I could avoid some of the kvm-depended behavior of midp GUI components. Anyone who has ever tried to develop using the standard GUI components will soon discover that its almost impossible to develop an application that looks and runs correctly on more that a few phone models, even models of the same manufacturer. 

The toy project evolved into the Fire library which was used extensively by me, my company and other mobile developers on tens of j2me projects over the past years. It is used as the GUI engine of the Bluevibe Browser (http://www.bluevibe.net). Apart from rendering the UI of the application and handling the animations, the xHTML component of the library is used by the Bluevibe Browser to render web pages delivered over Bluetooth or WiFi connections to the cell phones of all Bluevibe users.

Changelog since v2.2b:
-----------------------

The original plans where to build "custom font support" into the Fire2.3, but since it has been more than six months since the last release and I did not find yet the time to start working on the custom font implementation, I decided to make this release with all the accumulated micro improvements and bug fixes of the past 6 months. All the cool features like custom fonts and a revised rendering mechanism are now scheduled for the a next version. Below there is an (nearly complete) list of the improvements and bugfixes made since v2.2b:

- Added support for content Handlers. Added UnsupportedHandler class to handle unsupported http responses.
- FireScreen exposes getKeyStates method, improves game development support
- Improved Browser.loadImage, to work with some special cases of images.
- AsyncImageLoaded code is now part of the Page class
- New FireListener interface allows notifications about screen size changes.
- Improved support for canceling ongoing requests
- Diagonal dragging and scrolling
- Improved Alerts and Alert animations
- Improved transition animations
- Support for malformed html documents without a body tag
- FireScreen.getScreenshot() method for capturing screenshots
- Improved splash-screen and progress bar look and feel
- Added autoProgress functionality to progress bar.
- new RMS wrapper methods in FireConnector
- Added getCommmand() method to Component. 
- Increased ZINDEX_MAX to 11
- BugFix for animation queue
- BugFix, Browser would not stop rendering until END_OF_DOCUMENT was reached.
- BugFix for RIM BlackBerry softkeys.
- BugFix, InputComponent in text-area mode did not render correctly the text inside the Text Area.
- BugFix for Blackberry "Connection not Writable" exception
- BugFix for pointer events inside a container
- BugFix: Parsing of strange date fields in cookies



Changelog since v2.2:
----------------------

Since the release of v2.2 there where a some important bug fixes and other small additions to the API which are not part of the new features planned for v2.3. So all these are packaged in this minor release:

- Added a new method on the PageListener interface to allow an external gauge or progress bar
- Added a cancel method on the Browser to cancel the lates page load request.
- Code and samples clean-up to work with the above additions
- Bug fixes on the asynchronous image loading.
- Bug fix for meta refresh  
- UTF-8 literals changed to escape sequences (thnx timbob)


Changelog since v2.1:
-----------------------

The most important addition to this release is the landscape modes and the new way of handling softkeys.

- Working landscape modes (landscape modes where not available in v2.0 and v2.1).
- Softkeys are Components now (rather than just a special case)
- keyRepeated bug fixes
- FireScreen will generate keyRepeated events on devices that to not support it.
- Theme has setters allowing its properties to change programmatically.
- Theme createGradient bug-fix
- Various scrolling and navigation bug fixes.
- HttpClient's bug fix on current page after 404
- <title> tag text is now trimed before handling
- flag on Browser to switch "loagin gauge" off.
- Fixed bug of formated text not rendering properly in <li> 
- Fixed the bug in ImageComponent does not honor alt String in some cases 
- Added defaults colors in html components (default css values) 
- other minor improvements

Changelog since v2.0: 
----------------------- 

Many additions and bug fixes where made since v2.0. The main additions include better javadoc and more code samples. 

- New PageListener Interface.  Can be set to receive loadPageCompleted events. Also displayPage is now called loadPageAsync 
- animationsEvabled flag in the FireScreen disables the build-in animations like scroll when needed by the 
  developer (i.e when running on a low-end phone). 
- Alerts have been improved to be easier to use and customize. 
- Better scrolling and navigation inside panels. The scrolling logic of the Panel is dramatically improved, 
  increasing the navigation speed and improving the end-user experience. 
- Improved xHTML component. Numerous bug fixes on the Browser now allow access to many more sites. 
- Improved i18n. Bug fixes on the HttpClient and other utility classes improve language support 
- Common layout. Now the Panel and the Container class have the same layout schematic 
- GridLayout now supports both vertical and horizontal navigation. 
- many more minor fixes 


Usage: 
--------------- 

You can use the library by adding the compiled classes or source to your project. Check the code 
samples in the Fire2Samples project and the javadoc for more information on using fire. 

Please use the project's home page on sourceforge.net to post questions (on the forum) and report bugs (on the bug tracker tool). 


Demo: 
-------------- 

In the Fire2Samples.jar there are sample midlets that demonstrate the capabilities of the library. You can use a j2me emulator like the like SUN's WTK or microemulator or install it on a mobile phone that supports midp2 cldc1.0 midlets. 

Package Contents:
--------------------------

README.txt: This file
doc: Fire and code samples javadoc
src: source of Fire2 and of code samples
classes: compiled fire2 class files
demo: Demonstration midlet, from the samples project (both obfuscated and normal version)



Acknowledgments: 
------------------ 

Thanks to everyone who contributed patches and submitted bugs, for this release. 
Special thanks to George Saslis and George Peponakis from bluevibe.net for their great support, bug fixes and recommendations, also to Frank for his bug fixes and contributions and to Maxim Blagov for the motorola fix. 


I hope that you will find this project usefull. 

You can send feedback, bug reports etc using the project's homepage on sourceforge (preferred): 

http://sourceforge.net/projects/fire-j2me 

 - or - 

contact me: padeler at users.sourceforge.net


