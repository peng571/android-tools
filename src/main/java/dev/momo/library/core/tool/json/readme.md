# Json #

最後修改 2017/12/03 MOMO

為了減少置換library時，需要的修改，統一將json的出入口放在SimpleMapper.class。
並將被至換掉的檔案保留在json資料夾下。

目前使用過了Json Library有一下幾種，在這邊列出比較。

- Jackson
- Gson



## Jackson

在大檔案和小檔案都有不錯的效率，且有較好的擴充性，但library過於龐大(6000+ methods count)。


### 使用方法

基本的 Parse Method 請見 json/jackson/JacksonMapper.java

#### 需引用的library

// TODO

#### 物件

一些常用的屬性

- @JsonIgnore 加在Value的上方表示省略

// TODO
// ...

<!-- - 需要在物件開頭著名 @Jsonxxx... -->

## Gson [choosen]

在大檔案的表現比較優異，小檔案普普，但使用上簡單。


### Library Include

當前採用[2.8.2]

    compile 'com.google.code.gson:gson:[version]'


### 使用方法

> 詳見  json/jackson/GsonMapper.java

很簡單就可以取得可直接使用的gson物件。

    Gson gson = new Gson()
    String json = gson.toJson(obj);

Gson 在創建的時候，可直接定義許多設定值，這邊列出最常使用的設置。

    new GsonBuilder()

        // 忽略沒有加上@Expose的欄位，可大幅減少序列畫出來後的資料量
        .excludeFieldsWithoutExposeAnnotation()

        // 忽略 static or final 的欄位
        .excludeFieldsWithModifiers(Modifier.STATIC | Modifier.FINAL)

        // 調整印出的json格式
        .setPrettyPrinting()

        .create()


### 其他常用屬性

- @Expose(serialize = false) : 指定該屬性是否要序列化
    + serialize (boolean) 序列化 默认 true
    + deserialize (boolean) 反序列化 默认 true

- @SerializedName("name") : 可直接修改序列化依賴的名字