彰秀護理之家 醫療大數據APP Restful API 
==================================
## 環境
- Ubuntu 18.04
- MySQL 5.7.32
- NodeJS v10.23.0
## 建置
### 資料庫建置
**1. 安裝 MySQL**
```bash=
sudo apt-get install mysql-server     # 伺服端
sudo apt install mysql-client         # 客戶端
sudo apt install libmysqlclient-dev   # 一些MySQL資料庫的函式庫
```
**2. 開啟 MySQL 服務**
```bash=
sudo service mysql start 
```
**3. 測試資料庫是否開啟成功**
```bash=
sudo service mysql status 
sudo systemctl enable mysql # 預設開機自動啟動
```
**3. 測試資料庫能否連線成功**
```bash=
sudo apt-get install net-tools 
sudo netstat -tap | grep mysql # 查看已安裝好的 MySQL 是否有連線監聽 
```
**4. 進入 MySQL 服務**
![](https://i.imgur.com/IwJK6AV.png)
<center>

:arrow_up_small: 預設帳密在 _/etc/mysql/debian.cnf_
</center>

```bash=
mysql -u debian-sys-maint -p
```
**5. 創建資料庫 Schema**
```mysql=
CREATE DATABASE MedBigDataAI;
```
**6. 創建使用者**
```mysql=
GRANT ALL PRIVILEGES ON `MedBigDataAI`.* TO 'medical'@'localhost' IDENTIFIED BY 'ggininder' WITH GRANT OPTION;
flush privileges;  # 更新授權
exit;              # 退出服務
```
**7. 重啟 MySQL**
```bash=
service mysql restart # 重啟 MySQL 即可完成並生效所有的設定
```
**8. 還原初始資料庫**
專案資料夾裡有一個 data.sql 檔案為初始資料庫，來還原它
```bash=
mysql -u medical -p MedBigDataAI < data.sql 
# 預設密碼 ggininder
```
### RestFul API 建置
**1. 下載專案**
```bash=
cd /opt
git clone https://github.com/RAchange/my_android_v3
cd my_android_v3/RestfulAPI
mv medapp.sh /etc/init.d
```
**2. 安裝套件**
```bash=
curl https://npmjs.org/install.sh | sh # 套件管理工具
npm i                                  # 安裝 API 開發函示庫
npm install -g forever                 # 安裝 daemon 服務
```
**3. 開啟服務**
```bash=
sudo /etc/init.d/medapp.sh start   # 開啟服務
sudo /etc/init.d/medapp.sh stop    # 停止服務
sudo /etc/init.d/medapp.sh restart # 重啟服務
```
## 聯繫方式
:email:  ras@csie.io