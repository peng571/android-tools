# rx

> 這是筆記

####  Data Binding

替換掉原本的Butter Knife

https://developer.android.com/topic/libraries/data-binding/index.html

對google原生的物件有較高的支援度，可在xml完成資料的綁定，來達到MVVM的實作。


     惡補關於MVVM

    - M (Module)，商業模型，只負責資料的處理。
    - V (View)，實際的UI，只負責提供UI。
    - VM (View-Module)，虛擬的UI。


    (下) V -> VM -> M (上)

    三者的關係如上列所示，上級的物件可以存取下級，但下級的並不會知道上級的內容，藉此來達成乾淨的架構。

    V透過Binding提供給VM，並在V被更新的同時通知VM，而VM則在需要的時候透過M公開的方法，提交給M處理。


#### Gson or Jackson

詳見，tool/json底下Readme說明。


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
