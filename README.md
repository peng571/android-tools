# Core library

Core library code to create an android app


## Sub module

Use this project as application sub module by using git submodules

### When Push

change dir to sub module root project, then do as other project push

    cd [submodule root]
    git commit -a -m "commit message"
    git push

### When Pull

To update sub module, call sub module update at root project

    git submodule update

or, use recursive if update not work.

    git submodule update --recursive --remote


### When Clone

    git submodule init
    git submodule update --recursive --remote


## TODO

- finish all doc
- clean un useful library