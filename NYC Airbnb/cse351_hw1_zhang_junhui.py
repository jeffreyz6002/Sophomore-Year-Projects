coef = []
intercept = []
MSE = []
sum_MSE = 0
sum_RMSE = 0

kf = KFold(shuffle = True)
for train_index, test_index in kf.split(x):
    train, test = train_test_split(data)

    trainX = {}
    trainY = {}
    for ug in unique_genre:
        x = []
        y = []
        for l in range (0, len(train["genres"])):
            if (train["genres"].get(l) and train["revenue"].get(l) and ug in train["genres"].get(l)):
                x.append(train["budget"].get(l))
                y.append(train["revenue"].get(l))
        trainX[ug] = x
        trainY[ug] = y

    testX = {}
    testY = {}
    for ug in unique_genre:
        x = []
        y = []
        for l in range (0, len(test["genres"])):
            if (test["genres"].get(l) and test["revenue"].get(l) and ug in test["genres"].get(l)):
                x.append(test["budget"].get(l))
                y.append(test["revenue"].get(l))
        testX[ug] = x
        testY[ug] = y
for g in unique_genre:
    if(testX.get(g) and testY.get(g) and trainX.get(g) and trainY.get(g)):
        # Gets all coordinates for x and y 
        xTrain = np.array(trainX.get(g)).reshape((-1, 1))
        yTrain = np.array(trainY.get(g))
        xTest = np.array(testX.get(g)).reshape((-1, 1))
        yTest = np.array(testY.get(g))
        # Creates LinearRegression object
        m4 = LinearRegression()
        print(g)
        m4.fit(xTrain,yTrain)
        # Creates regression line based off coordinates
        pyplt.scatter(xTrain,yTrain)
        # Labels and organization
        pyplt.xlabel("Budget")
        pyplt.ylabel("Revenue")
        title = "Linear Regression by Genre: " + g
        pyplt.title(title)
        pyplt.show()
    
        # Gets variables for solving RMSE and MSE
        m4 = m4.fit(xTrain,yTrain)
        coef.append(m4.coef_[0])
        intercept.append(m4.intercept_)
        predictions = m4.predict(xTest)
        pyplt.plot(xTest, predictions, color="red")
        
        
        MSE = np.mean((yTest - predictions) ** 2)
        
        
        m4coef_ = np.array([np.mean(coef)])
        m4intercept_ = np.mean(intercept)
        print("The " + g + " movie linear regression model is: Revenue = " + str(m4.coef_[0]) + " * Budget + " + str(m4.intercept_))
        
        # MSE_average = np.mean(MSE)
        print("Average MSE, for model #4:", MSE_average)
        print("Average RMSE:", MSE ** 0.5 / 1000000, 'million\n')
        sum_RMSE += MSE_average ** 0.5 / 1000000
        sum_MSE += MSE_average
    
print("Average MSE across all genres is " +str(sum_MSE/len(allX)))
print("Average RMSE across all genres is " +str(sum_RMSE/len(allX)) + " million")