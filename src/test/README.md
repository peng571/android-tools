# 測試框架

> 進行中的文檔，紀錄關於測試的種種。

在android上常見的測試框架有以下幾種：

- junit
- mockito
- robolectric
- expresso

- power mock


## 架構

所有的測試程式所佔的比例最好是依照下面的比例，
由unit test佔最大宗，可以快速切明確的跑完大部分的流程，
再由少數的ui test在發布之前，完整但耗時的測試完整的app。

	ui test - 10%
	integration test - 20%
	unit test - 70%

其中，測試檔案需要依照android規範的位置放置。

- app/src/test/java : for any unit test which can run on the JVM
- app/src/androidTest/java : for any test which should run on an Android device


## Unit Test

單元測試。

- junit
- mockito (可隔離android sdk測試)

## Integration Test 

整合測試，整合多個unit test來測試一個完整的功能。


- Robolectric 