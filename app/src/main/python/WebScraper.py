import newspaper
from newspaper import news_pool
from newspaper import Article
from newspaper import Config
import re


# class WebScraper():
#     # Constructor, called when we start running the
#     def __init__(self):
#         #Download all new news articles from our sources
#         papers = [] #Holds all the models from all our news sources.
#         unseen_articles = []
#         #Build a model of all articles from the website. Get only those we haven't retrieved before
#         reuters_paper = newspaper.build('https://www.reuters.com/')
#
#         papers.append(reuters_paper)
#
#         news_pool.set(papers, threads_per_source=2)
#         news_pool.join()
#
#
#         for article in reuters_paper.articles:
#             article.parse()
#             unseen_articles.append(article.text)

sources = ['https://www.reuters.com/', 
        'https://www.bbc.co.uk/news', 
        'https://www.aljazeera.com/news/',
        'https://www.npr.org/sections/news/'
        ] #Holds all the models from all our news sources.

# Create our final dataframe
final_list_title = [] 
final_list_text = []
final_list_source =[]

def main():
    # Create our final dataframe
    #final_df = pd.DataFrame()

    # Create a download limit per sources
    # NOTE: You may not want to use a limit
    #limit = 10

    #for source in papers:
        # # temporary lists to store each element we want to extract
        # list_title = []
        # list_text = []
        # list_source =[]

        # count = 0
        
        # for article_extract in source.articles:
        #     #If the article title is already recorded, or it is not english, or it is too short to be real, ignore the article. or (len(str(article_extract.title).split(' ')) < 4)
        #     if((not re.search(r'[a-zA-Z]', str(article_extract.title)))): continue #Make sure it is English
        #     elif((str(article_extract.title) in final_list_title) ): continue #Make sure it isn't a duplicate
        #     elif((len(str(article_extract.title).split(' ')) < 4)): continue
        #     article_extract.parse()

        #     if count > limit: # Lets have a limit, so it doesnt take too long when you're
        #         break         # running the code. NOTE: You may not want to use a limit

        #     # Appending the elements we want to extract
        #     print(article_extract.title)
        #     final_list_title.append(article_extract.title)
        #     final_list_text.append(article_extract.text)
        #     final_list_source.append(article_extract.url)

        #     # Update count
        #     count +=1


        #temp_df = pd.DataFrame({'Title': list_title, 'Text': list_text, 'Source': list_source})
        # Append to the final DataFrame
        #final_df = final_df.append(temp_df, ignore_index = True)

        #It is possible that you may need to use the method below instead of the pool method, as the app stops after a while. 
        #Maybe if we set memoize_article=true, we can then repeat multiple loops in timeed intervals until
        #the new final_list_title is empty. Due to lack of license, you can only run python for 5 minutes
        #at a time. Use Thread.sleep() in java? You can use the time class to break the loop after 4 minutes?
        
        # source = papers[0]

        # count = 0
        # for i in range(len(source)):
        #     article_extract = source.article[i]
        #     if count > 5:
        #         break
        #     if((not re.search(r'[a-zA-Z]', str(article_extract.title)))): continue #Make sure it is English
        #     elif((str(article_extract.title) in final_list_title) ): continue #Make sure it isn't a duplicate
        #     elif((len(str(article_extract.title).split(' ')) < 4)): continue
        #     article = source.articles[i]
        #     try:
        #         article.download()
        #         article.parse()
        #     except :
        #         continue
        #     title = article.title
        #     source = article.url
        #     text = article.text

        #     final_list_title.append(title)
        #     final_list_source.append(source)
        #     final_list_text.append(text)
        #     count += 1

        for source in sources:
            iterativeDownload(source)

def iterativeDownload(sourceURL):

     # # temporary lists to store each element we want to extract
        # list_title = []
        # list_text = []
        # list_source =[]
    reuters_paper = newspaper.build(sourceURL, memoize_articles=False)
    source = reuters_paper#Reuters_paper is a list.

    for article_extract in source.articles:
        if((not re.search(r'[a-zA-Z]', str(article_extract.title)))): continue #Make sure it is English
        elif((str(article_extract.title) in final_list_title) ): continue #Make sure it isn't a duplicate
        elif((len(str(article_extract.title).split(' ')) < 4)): continue
        else:
            try:
                article_extract.download()
                article_extract.parse()
            except :
                continue
            title = article_extract.title
            source = article_extract.url
            text = article_extract.text

            final_list_title.append(title)
            final_list_source.append(source)
            final_list_text.append(text)
            

def pool():
    #Download all new news articles from our sources
    papers = []
    user_agent = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:78.0) Gecko/20100101 Firefox/78.0'

    config = Config()
    config.browser_user_agent = user_agent
    config.request_timeout = 10
    config.memoize_articles=False 

    #Build a model of all articles from the website. Get only those we haven't retrieved before
    reuters_paper = newspaper.build('https://www.reuters.com/', memoize_articles=False)#, request_timeout=10)
    bbc_paper = newspaper.build('https://www.bbc.co.uk/news', memoize_articles=False)#, request_timeout=10)

    #We add the models of the news sources
    papers.append(reuters_paper)
    papers.append(bbc_paper)

    news_pool.set(papers, threads_per_source=8)
    news_pool.join()

    return papers


def get_titles():
    return final_list_title
    
def get_texts():
    return final_list_text

def get_sources():
    return final_list_source

def get_papers():
    return sources

def add_papers(url):
    sources.append(url)

if __name__ == "__main__":
    # main(pool())
    for source in get_papers():
        iterativeDownload(source)
    for i in get_titles(): print(i)
    print(get_sources())
    print(len(get_titles()))