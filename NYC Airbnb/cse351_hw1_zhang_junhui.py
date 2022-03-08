from PIL import Image
from statistics import *
from wordcloud import WordCloud, STOPWORDS
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

# Question 1
def removeAnomaly(airbnb_data):
    # only NaN are reviews per month and are NaN since number of reviews == 0, so reviews per month must == 0
    airbnb_data.fillna(0)
    # removes row if avalibility_365 == 0, false data if location is never for rent
    remove_index = airbnb_data[airbnb_data['availability_365'] == 0].index
    airbnb_data.drop(remove_index, inplace = True)
    # remove outliers
    # with the given data, outliers can be detected in three ways, outragous standard deviation in both price and number of reviews, 
    # fitting data to be 1 percent in the largest or smallest part of the standard deviation
    Q1 = airbnb_data['price'].quantile(0.05)
    Q3 = airbnb_data['price'].quantile(0.95)
    IQR = Q3 - Q1
    airbnb_data_final = airbnb_data[~((airbnb_data['price']<(Q1-1.5*IQR)) | (airbnb_data['price']>(Q3+1.5*IQR)))]
    
    return airbnb_data_final

# Question 2A
def getTop5Bot5Price(airbnb_data):
    # sort data by neighbourhood and price since we are determining the top 5 and bottom 5 neighbourhoods based on price
    airbnb_filtered_neighborhood_price = airbnb_data.sort_values(["neighbourhood","price"],ascending = (True, False))
    airbnb_data_filtered = airbnb_filtered_neighborhood_price
    # gets the amount of houses there are in each neighbourhood since neighbourhoods with less than 5 hostings are invalid
    neighbourhoodCount = airbnb_data_filtered['neighbourhood'].value_counts()
    allPriceMeanList = []
    allNeighbourhoodsList = []
    # elminates all neighbourhoods that have less than 5 hostings
    for x,y in zip(airbnb_filtered_neighborhood_price['neighbourhood'], airbnb_filtered_neighborhood_price['neighbourhood'][1:]):
        if (neighbourhoodCount[x] >= 5 and x != y):
            allNeighbourhoodsList.append(x)
    # gets allPriceList which has all prices for the neighbourhoods selected into a dictionary
    allPriceList = {}
    for n in allNeighbourhoodsList:
        priceList = []
        for allN in range (0, len(airbnb_data['neighbourhood'])):
             if(airbnb_filtered_neighborhood_price['neighbourhood'].get(allN) == n):
                 priceList.append(airbnb_filtered_neighborhood_price['price'].get(allN))
        allPriceList[n] = priceList
    allPriceMeanList = {}
    # finds the mean of all neighbourhoods 
    for key, value in allPriceList.items():
        if len(value) > 1:
            allPriceMeanList[key] = mean(value)
    # sorts all the neighbourhoods and their average price and splits them into the bottom 5 and top 5 in price
    # then combines both structures of data into a dictionary for the user
    sortedMeanListLTH = dict(sorted(allPriceMeanList.items(), key=lambda item: item[1]))
    Lowest5Prices = list(sortedMeanListLTH.items())[:5]
    sortedMeanListHTL = sorted_d = dict(sorted(allPriceMeanList.items(), key=lambda item: item[1], reverse=True))
    Highest5Prices = list(sortedMeanListHTL.items())[:5]
    airbnb_data_neighbourhoods_filtered = airbnb_data
    top5NeighbourhoodsList = []
    bot5NeighbourhoodsList = []
    for x in range (len(Highest5Prices)):
        top5NeighbourhoodsList.append(Highest5Prices[x][0])
    for x in range (len(Lowest5Prices)):
        bot5NeighbourhoodsList.append(Lowest5Prices[x][0])
    top5bot5List = {"Top5": top5NeighbourhoodsList, "Bot5": bot5NeighbourhoodsList}
    return top5bot5List

# Question 2B
def plotTop5Bot5(top5bot5List, airbnb_data_neighbourhoods_filtered):
    # splits the top5 and bottom5 neighbourhoods into two arrays
    top5NeighbourhoodsList = top5bot5List.get("Top5")
    bot5NeighbourhoodsList = top5bot5List.get("Bot5")
    topAllInfo = []
    botAllInfo = []
    # puts all houses in top 5 and bottom 5 neighbourhoods into 2 arrays so that they can be distinguished
    for n in range (0, len(airbnb_data_neighbourhoods_filtered['neighbourhood'])):
        if airbnb_data_neighbourhoods_filtered['neighbourhood'].iloc[[n][0]] in top5NeighbourhoodsList:
            topAllInfo.append(airbnb_data_neighbourhoods_filtered.iloc[[n][0]])
        if airbnb_data_neighbourhoods_filtered['neighbourhood'].iloc[[n][0]] in bot5NeighbourhoodsList:
            botAllInfo.append(airbnb_data_neighbourhoods_filtered.iloc[[n][0]])       
    # gets all coordinates of hostings into 4 different arrays
    topX = []
    topY = []
    botX = []
    botY = []
    for i in range (0, len(topAllInfo)):
        for j in range (0, len(topAllInfo[i])):
            if j == 7:
                topX.append(topAllInfo[i][7])
            if j == 6:
                topY.append(topAllInfo[i][6])
    for i in range (0, len(botAllInfo)):
        for j in range (0, len(botAllInfo[i])):
            if j == 7:
                botX.append(botAllInfo[i][7])
            if j == 6:
                botY.append(botAllInfo[i][6])
    # plots the scatterplot with image provided
    nyc_map = Image.open("New_York_City_.png")
    plot = plt.imshow(nyc_map,zorder= 0,extent=[-74.24442,-73.71299,40.49979,40.91306])
    plt.scatter(topX, topY, color="blue",label='Top 5')
    plt.scatter(botX, botY, color="red",label='Bottom 5')
    plt.legend(loc="upper left")
    plt.title("Top/Bottom 5 Neighbourhoods")
    plt.xlabel("X")
    plt.ylabel("Y")
    
    # According to the data given from question 2, it seems to be true that the more popular or rich the area is in NYC, the higher the 
    # prices are for the airbnb. In other words, assuming that the center of the city is in Manhattan, the prices are highest at the center     # and the averages prices die down. Being farther from the center seems to result in a lower price for airbnbs. However, there is an 
    # exception as of the outlier of the location in Riverdale, Bronx. Additionally, it seems that the top 5 neighbourhoods seem to have a       # larger gap in average price than the bottom 5 priced neighbourhoods. This data can be seen more clearly in Highest5Prices and 
    # Lowest5Prices.
    
    # Question 3 
def createCorrelationHeatMap(airbnb_data):
    ax = plt.axes()
    ax.set_title('Correlations')
    corr = airbnb_data.corr()
    sns.heatmap(corr, xticklabels=corr.columns,yticklabels= corr.columns, annot=True, cmap=sns.diverging_palette(220, 20, as_cmap=True));
    
    # Question 4A
def plotByBorough(airbnb_data):
    airbnb_data_neighbourhoodGroups = airbnb_data.sort_values(["neighbourhood_group"],ascending = (True))
    # sets size of map
    plt.figure(figsize=(12,12))
    # imports image 
    nyc_map= Image.open("New_York_City_.png")
    # sets boundaries
    plt.imshow(nyc_map,zorder= 0,extent=[-74.24442,-73.71299,40.49979,40.91306])
    ax= plt.gca()
    # plots graph
    plot = sns.scatterplot(x= 'longitude', y='latitude',hue= 'neighbourhood_group',edgecolor='black',linewidth=0.3, data=airbnb_data_neighbourhoodGroups)
    plt.legend(loc='upper left')
    plt.title("All AirBnb Locations")
    
    # Question 4B
def plotByPrice(airbnb_data):
    # removes all prices above 1000
    remove_index = airbnb_data[airbnb_data['price'] >= 1000].index
    airbnb_data.drop(remove_index, inplace = True)
    # sets size of map
    plt.figure(figsize=(12,12))
    # imports image 
    nyc_map= Image.open("New_York_City_.png")
    # sets boundaries
    plt.imshow(nyc_map,zorder= 0,extent=[-74.24442,-73.71299,40.49979,40.91306])
    plt.title("AirBnb Locations By Price")
    ax= plt.gca()
    # plots graph
    airbnb_data.plot(kind='scatter', x='longitude', y='latitude', c='price',ax = ax, cmap=plt.get_cmap('summer'), colorbar=True, zorder=5);
   
    
    # Question 5
def generateWordCloud(airbnb_data):
    comment_words = ''
    # get commonly used words
    stopwords = set(STOPWORDS)
    text = " ".join(review for review in airbnb_data['name'].astype(str))
    # generate wordcloud
    wordcloud = WordCloud(stopwords=stopwords, background_color="white", width=800, height=400).generate(text)
    # display
    plt.axis("off")
    plt.figure( figsize=(40,20))
    plt.tight_layout(pad=0)
    plt.imshow(wordcloud, interpolation='bilinear')
    plt.show()
    
    # Question 6
    # plots the scatterplot based off the differences in the value of c
def generatePlotByListings(airbnb_data):
    plt.figure(figsize=(12,12))
    plt.title("AirBnb Locations By Number of Listings")
    nyc_map= Image.open("New_York_City_.png")
    plt.imshow(nyc_map,zorder= 0,extent=[-74.24442,-73.71299,40.49979,40.91306])
    ax= plt.gca()
    airbnb_data.plot(kind='scatter', x='longitude', y='latitude', c='calculated_host_listings_count',ax = ax, cmap=plt.get_cmap("coolwarm"), colorbar=True, zorder=2);
    
def generatePlotByAvailability(airbnb_data):
    plt.figure(figsize=(12,12))
    plt.title("AirBnb Locations By Availabilty")
    nyc_map= Image.open("New_York_City_.png")
    plt.imshow(nyc_map,zorder= 0,extent=[-74.24442,-73.71299,40.49979,40.91306])
    ax= plt.gca()
    airbnb_data.plot(kind='scatter', x='longitude', y='latitude', c='availability_365',ax = ax, cmap=plt.get_cmap("coolwarm"), colorbar=True, zorder=2);
    
def generatePlotByPrice(airbnb_data):
    plt.figure(figsize=(12,12))
    plt.title("AirBnb Locations By Price")
    nyc_map= Image.open("New_York_City_.png")
    plt.imshow(nyc_map,zorder= 0,extent=[-74.24442,-73.71299,40.49979,40.91306])
    ax= plt.gca()
    airbnb_data.plot(kind='scatter', x='longitude', y='latitude', c='price',ax = ax, cmap=plt.get_cmap("coolwarm"), colorbar=True, zorder=2);
    
def generatePlotByReviews(airbnb_data):
    plt.figure(figsize=(12,12))
    plt.title("AirBnb Locations By Number of Reviews")
    nyc_map= Image.open("New_York_City_.png")
    plt.imshow(nyc_map,zorder= 0,extent=[-74.24442,-73.71299,40.49979,40.91306])
    ax= plt.gca()
    airbnb_data.plot(kind='scatter', x='longitude', y='latitude', c='number_of_reviews',ax = ax, cmap=plt.get_cmap("Pastel2"), colorbar=True, zorder=2);

    # Computes the pearson correlation formula
def findCorrelation(airbnb_data, t1, t2):
    # Finds the average of the values of t1 thru the dataframe
    a1 = 0
    count = 0
    for i in range (0, len(airbnb_data[t1])):
        if(airbnb_data[t1].get(i) or airbnb_data[t2].get(i)):
            a1 = a1 + airbnb_data[t1].get(i)
            count += 1
    avg1 = a1 / count
    # Finds the average of the values of t2 thru the dataframe
    a2 = 0
    count = 0
    for i in range (0, len(airbnb_data[t2])):
        if(airbnb_data[t1].get(i) or airbnb_data[t2].get(i)):
            a2 = a2 + airbnb_data[t2].get(i)
            count += 1
    avg2 = a2 / count
    # calculates the numerator 
    numerator = 0 
    for i in range (0, len(airbnb_data[t2])):
        if(airbnb_data[t1].get(i) or airbnb_data[t2].get(i)):
            x = airbnb_data[t1].get(i) - avg1
            y = airbnb_data[t2].get(i) - avg2
            numerator += x*y
    # calculates the denominator
    denomintor = 0
    tempX = 0
    tempY = 0
    for i in range (0, len(airbnb_data[t1])):
        if(airbnb_data[t1].get(i) or airbnb_data[t2].get(i)):
            tempX += (airbnb_data[t1].get(i) - avg1)**2
            tempY += (airbnb_data[t2].get(i) - avg2)**2
    denominator = (tempX * tempY)**(1/2)
    result = numerator/denominator
    return result

    # Using the factors of number of reviews, price and availability, it seems like the most correlated data between the business and host listings is the availability. This can be proven by the large amount of blue on the generatePlotByAvilability graph and by the highest correlations with the highest listings count of 0.19 by using the table from question 3. Moreover, price also has a similar correlation with the host listings count with a correlation of 0.15.
    
    # Question 7
def generatePlotByRoomType(airbnb_data):
    airbnb_data_neighbourhoodGroups = airbnb_data.sort_values(["room_type"],ascending = (True))
    # sets size of map
    plt.figure(figsize=(12,12))
    # imports image 
    nyc_map= Image.open("New_York_City_.png")
    # sets boundaries
    plt.imshow(nyc_map,zorder= 0,extent=[-74.24442,-73.71299,40.49979,40.91306])
    plt.title("AirBnb Locations By Room Type")
    ax= plt.gca()
    # plots graph
    plot = sns.scatterplot(x= 'longitude', y='latitude',hue= 'room_type',edgecolor='black',linewidth=0.3, data=airbnb_data_neighbourhoodGroups)
    plt.legend(loc='upper left')
    
    # The two plots I decided to use to reveal something interesting is the price plot and the room type plot. From these two plots, I have learned that despite having a lot of shared rooms in the middle of Manhattan,indicated by the green, customers are still willing to pay more than for almost any other location which is indicated by the light green. From these two plots, this really shows that paying for a prime location in one of the largest cities in the world is worth it for many.
    
    
    