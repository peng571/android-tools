# Core library

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[ ![Download](https://api.bintray.com/packages/peng571/maven/tool-core/images/download.svg) ](https://bintray.com/peng571/pengyr_library/tool-core/_latestVersion)


用來幫助Android Application快速建立的Library。

## Import

#### Gradle

if useing gradle, add this line into apps build.gradle

    implementation 'org.pengyr.tool:tool-core:[VERSION]'


## Usage

#### AppHelper

set application context to init AppHelper

    AppHelper.initInstance(applicationContext)
    
#### Logger

setup logger with main tag to better filter on Logcat

    Logger.setTAG(YOUR_MAIN_TAG)
    Logger.setLogLevel(if (BuildConfig.DEBUG) Logger.LOG_ALL else Logger.LOG_NONE)


## License

```
Copyright Peng Yurou 2018

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
