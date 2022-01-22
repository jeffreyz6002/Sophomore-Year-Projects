from PIL import Image
from statistics import *
import pandas as pd
import numpy as np
import datetime as datetime1
from datetime import datetime as datetime2
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import precision_score
from sklearn.metrics import recall_score
from sklearn.metrics import accuracy_score
from sklearn.metrics import confusion_matrix
from sklearn.metrics import f1_score
from sklearn import metrics
import matplotlib.pyplot as plt
import csv
import seaborn as sns
import math

# Q1
weather_data = pd.read_csv("weather_data.csv")
energy_data = pd.read_csv("energy_data.csv")
energy_usage = {}
lastDate = 0
for i in range (0, len(energy_data["Date & Time"])):
    temp = datetime1.datetime.strptime(energy_data["Date & Time"].get(i),"%Y-%m-%d %H:%M:%S")
    new_format = "%Y-%m-%d"
    date = temp.strftime(new_format)
    temp = datetime1.datetime.strptime(energy_data["Date & Time"].get(i),"%Y-%m-%d %H:%M:%S")
    new_format = "%H:%M:%S"
    time = temp.strftime(new_format)
    if (date != lastDate):
        lastDate = date
        totalEnergyUsed = energy_data["use [kW]"].get(i)
    totalEnergyUsed += energy_data["use [kW]"].get(i)
    if (time == "23:30:00"):
        energy_usage[date] = totalEnergyUsed

#energy_usage 

weather_column = []
for row in weather_data:
    weather_column.append(row)
    
weather_column.pop(1)
weather_column.pop(3)
weather_column.pop(6)
allData = {}
weatherData = {}
#weather_column

# "and merge it with weather data"
for i in range (0, len(weather_data["time"])):
    dt = datetime2.utcfromtimestamp(weather_data["time"].get(i)).strftime('%Y-%m-%d %H:%M:%S')
    temp = datetime1.datetime.strptime(dt,"%Y-%m-%d %H:%M:%S")
    new_format = "%Y-%m-%d"
    date = temp.strftime(new_format)
    temp = datetime1.datetime.strptime(energy_data["Date & Time"].get(i),"%Y-%m-%d %H:%M:%S")
    new_format = "%H:%M:%S"
    time = temp.strftime(new_format)
    if(time == "00:00:00" or time == "12:00:00"):
        allData[date] = []
        
for c in weather_column:
    for i in range (0, len(weather_data["time"])):
        dt = datetime2.utcfromtimestamp(weather_data["time"].get(i)).strftime('%Y-%m-%d %H:%M:%S')
        temp = datetime1.datetime.strptime(dt,"%Y-%m-%d %H:%M:%S")
        new_format = "%Y-%m-%d"
        date = temp.strftime(new_format)
        temp = datetime1.datetime.strptime(energy_data["Date & Time"].get(i),"%Y-%m-%d %H:%M:%S")
        new_format = "%H:%M:%S"
        time = temp.strftime(new_format)

        if(time == "00:00:00" or time =="12:00:00"): # Indication of new day
            weatherData[c] = []

        if(weather_data[c].get(i)):
            if(math.isnan(weather_data[c].get(i))):
                weatherData[c].append(0)
            else:
                weatherData[c].append(weather_data[c].get(i))


        if(time == "23:30:00" or time == "11:30:00"): # Indication of end of day
            if(weatherData[c] == []):
                weatherData[c] = [0]
            weatherData[c] = mean(weatherData[c])
            allData[date].append(weatherData[c])

#allData
dateList = ["date"]
weather_column.append("use [kW]")
weather_column = dateList + weather_column
allDates = list(allData.keys())
#allDates

with open('newData.csv', 'w', encoding='UTF8', newline='') as f:
    writer = csv.writer(f)

    # write the header
    writer.writerow(weather_column)
    
    for ad in range (0, len(allData)):
        starter = [allDates[ad]]
        end = [energy_usage.get(allDates[ad])]
        middle = allData.get(allDates[ad])
        row = starter + middle + end
        # write the data
        writer.writerow(row)
    f.close()
    
# Q2
with open('decemberData.csv', 'w', encoding='UTF8', newline='') as f:
    writer = csv.writer(f)
    # write the header
    writer.writerow(weather_column)
    
    with open('newData.csv') as f:
        readerNewData = csv.reader(f)
    
        for i in readerNewData:
            if("2014-12" in i[0]):
                writer.writerow(i)
        f.close()
        
newData = pd.read_csv("newData.csv")
#newData

decemberData = pd.read_csv("decemberData.csv")
#decemberData

# train contains the training set, test contains the test set
train, test = train_test_split(decemberData)

# Q3
testUseKW = test["use [kW]"]
testDate  = test["date"]
#testUseKW

del test["use [kW]"]
#test

sns.heatmap(train.corr(), cmap='RdBu', center=0)

m = LinearRegression()

xTrain = train["humidity"].to_numpy().reshape(-1,1)
yTrain = train["use [kW]"].to_numpy().reshape(-1,1)
xTest = test["humidity"].to_numpy().reshape(-1,1)
yTest = testUseKW.to_numpy().reshape(-1,1)

m = m.fit(xTrain,yTrain)
coef = m.coef_
intercept = m.intercept_
print("The linear model is: used[kW] = " + str(coef[0][0]) + " * Humidity + " + str(intercept[0]))

predictions = m.predict(xTest)
MSE = np.mean(yTest - predictions) ** 2
RMSE = np.sqrt(MSE)
print("MSE: " +str(MSE))
print("RMSE: " +str(RMSE))
predictions = m.predict(decemberData["humidity"].to_numpy().reshape(-1, 1))
predictions1d = []
for i in predictions:
    predictions1d.append(i[0])

            
sns.scatterplot(train["humidity"], train["use [kW]"],label="Training Set")
sns.scatterplot(decemberData["humidity"], predictions1d, label="Prediction Line")
sns.scatterplot(test["humidity"], testUseKW, color="red", label="Test Set")
plt.title("Baseline Model: Humidity Vs use [kW]")
plt.legend(bbox_to_anchor= (1,1))

with open('cse351_hw2_Zhang_Junhui_112895310_linear_regression.csv', 'w', encoding='UTF8', newline='') as f:
    writer = csv.writer(f)
    # write the header
    writer.writerow(["Date", "Predicted Use [kW]"])
    for i in range(0, len(decemberData["date"])):
        input = []
        input.append(decemberData["date"].get(i))
        x = decemberData["humidity"].get(i)
        b = intercept[0]
        m = coef[0][0]
        inputPredict = m * x + b
        input.append(inputPredict)
        writer.writerow(input)
        
# Q4
train, test = train_test_split(decemberData)
for i in decemberData["date"]:
    decemberData["date"] = decemberData["date"].replace(i, str(i).replace("-",""))
    
x = decemberData["date"]
y = decemberData["temperature"].round(0)

over35X = []
under35X = []
over35Y = []
under35Y = []
for i in range (0,len(x)):
    if (int(y.get(i)) >= 35):
        over35X.append(x.get(i))
        over35Y.append(y.get(i))
    else:
        under35X.append(x.get(i))
        under35Y.append(y.get(i))

for i in train["date"]:
    train["date"] = train["date"].replace(i, str(i).replace("-",""))
    
for i in test["date"]:
    test["date"] = test["date"].replace(i, str(i).replace("-",""))
    
xTrain = train["date"].to_numpy().reshape(-1,1)
yTrain = train["temperature"].round(0).to_numpy().reshape(-1,1)
xTest = test["date"].to_numpy().reshape(-1,1)
yTest = test["temperature"].round(0).to_numpy().reshape(-1,1)
m2 = LogisticRegression()
m2 = m2.fit(xTrain,yTrain)
x2 = x.to_numpy().reshape(-1,1)
y_pred = m2.predict(x2)

plt.scatter(under35X,under35Y,color="red",label="< 35")
plt.scatter(over35X,over35Y,color="blue",label=">= 35")
plt.scatter(test["date"],test["temperature"],color="orange",label="Test Set")
plt.scatter(x,y_pred,color="green",label="Logistic Regression")
plt.title("Date to Temperature Scatter Plot")
plt.xticks(rotation=90)
plt.xlabel("Dates")
plt.ylabel("Temperature")
plt.legend(loc="upper left")
plt.show()

coef = m2.coef_
intercept = m2.intercept_

fscore = str(f1_score(y, y_pred, average="macro"))
print("F-Score: "+fscore)

decemberDates = []
for i in allDates:
    if("2014-12-" in i):
        decemberDates.append(i)
        
with open('cse351_hw2_Zhang_Junhui_112895310_logistic_regression.csv', 'w', encoding='UTF8', newline='') as f:
    writer = csv.writer(f)
    # write the header
    writer.writerow(["Date", "Classification Of Temp"])
    for i in range(0, len(decemberData["date"])):
        input = []
        input.append(decemberDates[i])
        if(decemberData["temperature"].get(i) >= 35):
            input.append(1)
        else:
            input.append(0)

        writer.writerow(input)
        
# Q5
#energy_data

furnaceDayNightData = {}
washerDayNightData = {}
furnaceEnergy = []
washerEnergy = []
for i in range (0, len(energy_data["Date & Time"])):
    temp = datetime1.datetime.strptime(energy_data["Date & Time"].get(i),"%Y-%m-%d %H:%M:%S")
    new_format = "%Y-%m-%d"
    date = temp.strftime(new_format)
    temp = datetime1.datetime.strptime(energy_data["Date & Time"].get(i),"%Y-%m-%d %H:%M:%S")
    new_format = "%H:%M:%S"
    time = temp.strftime(new_format)
    
    if("06:00:00" in time):
        if(furnaceEnergy or washerEnergy):
            if(len(furnaceEnergy) != 0 or len(washerEnergy) != 0):
                temp = date + " day"
                furnaceDayNightData[temp] = furnaceEnergy
                washerDayNightData[temp] = washerEnergy
            furnaceEnergy = []
            washerEnergy = []
               
    if("19:00:00" in time):
        if(len(furnaceEnergy) != 0 or len(washerEnergy) != 0):
            temp = date + " night"
            furnaceDayNightData[temp] = furnaceEnergy
            washerDayNightData[temp] = washerEnergy
        furnaceEnergy = []
        washerEnergy = []
        
    furnaceEnergy.append(energy_data["Furnace [kW]"].get(i))
    washerEnergy.append(energy_data["Washer [kW]"].get(i))
    
#furnaceDayNightData
washerDayEnergy = 0
washerNightEnergy = 0
furnaceDayEnergy = 0
furnaceNightEnergy = 0 

keys = list(furnaceDayNightData.keys())
for k in keys:
    washerL = washerDayNightData.get(k)
    sum = 0
    for i in washerL:
        sum += i
    if("day" in k):
        washerDayEnergy += sum
    else:
        washerNightEnergy += sum
        
    furnaceL = furnaceDayNightData.get(k)
    sum = 0
    for i in washerL:
        sum += i
    if("day" in k):
        furnaceDayEnergy += sum
    else:
        furnaceNightEnergy += sum
        
print("WasherDay: " +str(washerDayEnergy))
print("washerNight: "+str(washerNightEnergy))
print("furnanceDay: "+str(furnaceDayEnergy))
print("furnacneNight: "+str(furnaceNightEnergy))

xAxis = ("Washer:Day","Washer:Night","Furnace:Day","Furnace:Night")
yAxis = [washerDayEnergy,washerNightEnergy,furnaceDayEnergy,furnaceNightEnergy]
plt.bar(xAxis,yAxis, align="center")
plt.title("Energy By Device During Day/Night")
plt.ylabel("Energy Used in kW")
plt.xlabel("Device:Time Of Day")
plt.show()
#- Is the washer being used only during the day?
#During what time of the day is AC used most?