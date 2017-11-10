# Json #

最後修改 2017/11/10 MOMO

為了減少置換library時，需要的修改，統一將json的出入口放在SimpleMapper.class。
並將被至換掉的檔案保留在json資料夾下。


## API ##

在這邊紀錄所使用過的json library。

### Jackson

在大檔案和小檔案都有不錯的效率，且有較好的擴充性，但library過於龐大(6000+ methods count)。

#### 需引用的library



#### 物件

    @JsonIgnore



### Gson [choosen]

在大檔案的表現比較優異，小檔案普普，但使用上簡單。


#### Library

當前採用[2.8.2]

    compile 'com.google.code.gson:gson:[version]'