# 小组项目作业 仿QQ聊天程序 使用了加密算法

## 它是怎么运作的？
### 公共类包
- user类：提供进行user操作的一些共性，并作为实例对象的整体进行数据传输。
- message类：提供运行message传输的一些共性，并作实例对象的整体进行数据传输。
- messageType接口：表示message的种类。
### 服务端
- 打开QQserver的调试类开始工作。然后开始创建socket类实例的接收端。在Server中主要进行登录与注册操作。
- 接收到message数据后，得到其中的ID与密码，并准备好输出流、[数据库链接](#数据库链接)与message类进行输出准备。
- 注册部分：拉取数据库，检测ID存不存在，存在就报错，不存在就登入信息并同时进行登录操作。
- 登录部分：检测匹不匹配，匹配则开启一个属于该用户的线程。（Socket仅用于传输接收登录相关数据，端对端对话在客户线程中实现）
- *close*类用于关闭服务器。但这样需要自己手写一个控制台命令才好用，感兴趣的话可以试试。
### 数据库链接
*备注：在使用该类前，需要先下载一种SQL（我用的MySql）并将root账号的密码设为123456，或者把SqlHelper中的password变量改成密码*
- 进入构造方法，用*getConnection*方法来获得链接。
- *InsertData*方法用来插入数据。
- *queryExecute*方法来查询数据库中的数据。
- *close*方法用来关闭链接。