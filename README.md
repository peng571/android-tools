# Core library

> in branch submodule

最後修改日期 2017/12/03 MomoPeng

用來幫助Android Application快速建立的Library。

## TODO

- 完成所有文件說明。
- 整理未使用的物件和library。

 
## 使用方法

建議使用git submodule來使用這些library。

#### 使用方法筆記

進入submoduel的資料夾，之後使用方法皆同一般的專案，記得要先切換到master，或者是目的的branch，反則如果在原本的專案中切換branch時，可能會造成submodule內修改到一半的程式碼進入沒有指標的狀態而丟失。

    cd [submodule dir]
    git checkout master
    git pull
    git add .
    git commit -m "comment message"
    cd ..

之後回到原本的專案資料夾，需要再提交submodule的更新(''這邊不確定，在查查~'')，切換branch的時候記得確認所使用的submodule是否為預期的版本和分支。

可用這行程式碼來更新所有submodule

    git submodule foreach git pull origin master


#### Gson or Jackson

詳見，tool/json底下Readme說明。