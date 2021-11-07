# Boat_H2O2 Pro  
基于boat app为核心的运行在安卓设备上的Minecraft启动器。  
[视频教程](https://m.bilibili.com/video/BV1TM4y1N7j2?p=1&share_medium=android&share_plat=android&share_source=COPY&share_tag=s_i&timestamp=1627515206&unique_k=ea3HRj&share_times=1)  
![2dcode](./markdown/author.jpg)  
## 功能介绍  

### 已开发功能  

* 运行Minecraft Java版1.6 - 20w09a  
* 版本隔离  
* 全键盘支持  
* Material You主题切换  
* 微软登录  
* mojang登录  
* 外置登录  
* 原版下载  
* 终端  
* 模组管理  
* 日志查看  
* 崩溃查看  
* 内置整合包导出  

### 未来更新内容

* 更多资源管理
* curseforge资源支持  
* mcmod百科查看  
* Easy wiki  
* 模组API安装  
* 整合包安装  
* 自定义按键  
* 20w10a+ 支持  
......  

## 使用说明  

### 特别提示：  

* <font color=#FF000 >3.3.8之前版本的在更新之前需要卸载旧版。</font>  

### 安装后：  

授予读写权限，勾选不再显示此界面  
![2dcode](./markdown/1.png)  
![2dcode](./markdown/2.png)  

### 进入启动器：  

如果不出意外正常进入启动器主页  
如果闪退，请检查是否有权限！  

### 账号登录：  

![2dcode](./markdown/10.png)  
点击第一个卡片进行账号管理  
账号分为离线和在线模式：   
打开在线登录开关会使主页第一个卡片变为绿色，反之为蓝色。 
 ![2dcode](./markdown/20.png)  
在线：  
* 如果未登录，会显示三个输入框，第一个填写邮箱账号，第二个填写密码器，第三个选择API。点击第三个输入框会弹出对话框，此时选择相应的API，分别是mojang、微软、第三方登录。需要第三方登录的用户，请确保账号内仅拥有一个用户名，否则会报错（后续会支持多用户）。若不确定，可以点击访问API所属网站进行查询。  
离线： 输入用户名即可。  
* 信息填完毕之后请点击保存账号信息（登录成功才会真正保存）。  
![2dcode](./markdown/4.png)  
![2dcode](./markdown/5.png)  
登录：  
* 点击第二个卡片进行登录。  
> 注意： 请先保证当前目录内有一个游戏版本才能进行登录，否则会提示没有版本。  

### 安装游戏  

原版：  
* 推荐启动器内安装，选择1.6 - 21w09a 之间的版本。  
* 点击主页第二个卡片的安装按钮或者启动器安装页面点击Minecraft进入下载页。点击列表上方的按钮选择mcbbs源，然后找到你想安装的原版，点击下载即可。如果卡顿请检查网络、下载源是否正确，若无问题点击刷新按钮。  
![2dcode](./markdown/6.png)  
![2dcode](./markdown/7.png)  
![2dcode](./markdown/8.png)  

模组API/整合包：  
* 暂不支持启动器内部安装。请将模组整合包解压，将其中的.minecraft或gamedir文件夹的路径复制好，在版本列表页面（主页-目录/目录-版本列表）点击下方下方按钮添加目录粘贴确定即可。  
![2dcode](./markdown/9.png)  

启动器内整合包导出：  
* 有点问题，暂时不开放。  

路径切换：  
* 新增路径后可以在版本列表的游戏目录列表中查看，点击项目进行切换。 点击右上方的tab栏查看当前路径版本列表，点击进入模组管理。  
![2dcode](./markdown/11.png)  
![2dcode](./markdown/12.png)  

版本隔离：  
* 设置-版本隔离，将版本核心文件、模组、资源包等单独运行，与其他版本互不干扰。  

模组： 
* 版本列表-点击版本进入，若标题出现版本隔离字样则开启了版本隔离。右上角可导入。   
![2dcode](./markdown/13.png)  
![2dcode](./markdown/14.png)  
![2dcode](./markdown/15.png)  

主题： 
* Pixel主屏幕-长按-壁纸和样式-壁纸颜色。   
![2dcode](./markdown/16.png)  
![2dcode](./markdown/19.png)  

### 运行游戏  

* 登录成功之后会显示一个对话框选择键鼠或者触控模式。
* 键鼠有bug暂不提供教程。触控则正常体验触屏。  

### 启动参数

如果不小心搞坏了启动器，也有可能是乱动启动参数。这时请在设置中更改成默认的。  

JVM参数：  
```
-client -Xmx750M
```
MC参数：  
把内容全部删掉。  
![2dcode](./markdown/17.png)  

## 崩溃帮助  

### 启动器崩溃  

会弹出错误页面，可直接查看错误原因，将它发送给开发者。  

### 游戏崩溃  

设置-查看日志，可查看客户端client_outpt.txt和启动器输出日志log.txt，位置在  
> /storage/emulated/0/games/com.koishi.launcher/h2o2  

发送给开发者。  
![2dcode](./markdown/18.png)  

## 版本1.0  
* 文档内容会在后续功能加入之后持续更新。  
