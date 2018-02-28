# LooForYou
The toilet app you want.. and also the one you need

## Git Instructions: mini cheatsheet of the most important git code
----
#### Terms
* **master branch** - branch with latest working code (main program)
* **remote branch** - your own branch
* **upstream branch** - branch that can be tracked
* **origin** - perform operation on the source of your target


### going to a branch:
* this is your private workspace
```
git checkout {branchname}
```

### going to a *new* branch
* making a new branch with all the work you have
```
git checkout -b {newbranchname}
```

### when pushing to your branch for the first time
* must set upstream so that github can track it 
```
git push -u origin {branchname}    //you only need the -u argument in your very first push. 
```

### pushing to a branch
* whenever you push, you should ALWAYS **ADD**, **COMMIT**, then **PULL** first
```
git add .                    //period means "everything". you can also choose to add only specific files
git commit -m "message"      //**REQUIRED** you need to type in a message for every commit
git pull origin master       //this pulls code FROM master TO whatever branch you're on (if you are already on master branch, keyword origin is optional)
//ALWAYS pull before you push (make sure to add and commit first) to avoid overriding data
git push                     //push to your branch
```
* order of operations:   	 add -> commit -> pull -> push

________________________________________________________________________________________________________________________________________________________________________

###ssh into server for the first time

*If you don't have bash install from here: https://docs.microsoft.com/en-us/windows/wsl/install-win10

*Download and install Ubuntu for Windows 10.

*Make sure you have the file looforyou.pem in current directory.

*From bash, type in commands
```
$ chown :root looforyou.pem
$ chmod 600 looforyou.pem
$ ssh -i looforyou.pem ubuntu@ec2-18-144-72-20.us-west-1.compute.amazonaws.com
```








