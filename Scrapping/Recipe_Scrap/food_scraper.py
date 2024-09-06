import requests
from bs4 import BeautifulSoup
import time
import random
import configparser

from file_storage import FileStorage
from recipe import Recipe

# import the config setting from the config.ini
config = configparser.ConfigParser()
config.read('./config.ini')
FILE_NAME = config.get('Settings', 'FILE_NAME')


class FoodScraper:
    def __init__(self, search_url):
        self.search_url = search_url

    # make the HTTP POST request and return the response in json
    def fetch_search_results(self, page_number):
        # specify the HTTP request header
        payload = {
            "contexts": [],
            "searchTerm": "",
            "pn": page_number
        }
        try:
            response = requests.post(self.search_url, json=payload)
            # return the response when http connection is working correctly
            if response.status_code == 200:
                return response.json()
            else:
                print(f"Failed to fetch search results for page {page_number}: {response.status_code}")
                return None
        except Exception as e:
            print(f"An error occurred while fetching search results for page {page_number}: {e}")
            return None

    # get the subpage url from the response
    def parse_search_results(self, search_results):
        if search_results and 'response' in search_results:
            results = search_results['response']['results']
            subpage_urls = [result['record_url'] for result in results]
            return subpage_urls
        else:
            return []
    # make the HTTP get request to the subpage and fetch the content
    def fetch_subpage(self, url):
        try:
            response = requests.get(url)
            if response.status_code == 200:
                return response.content
            else:
                print(f"Failed to fetch subpage {url}: {response.status_code}")
                return None
        except Exception as e:
            print(f"An error occurred while fetching subpage {url}: {e}")
            return None

    # get the information from the subpage and save into the recipe structure
    def parse_subpage(self, html_content):
        if html_content:
            soup = BeautifulSoup(html_content, 'html.parser')
            title = soup.find('h1').text.strip()
            img_src = soup.find('img', style='--aspect-ratio: 5/4').get('src')
            dd_tags = soup.find_all('dd', class_='facts__value svelte-1dqq0pw')
            # get the expected preparation time
            ready_in = dd_tags[0].text.strip()
            ingredient_num = dd_tags[1].text.strip()
            # ingredients
            ingredients = [a.text for a in soup.find('ul', class_='ingredient-list svelte-1dqq0pw').find_all('a')]
            # get the step by step instructions on how to make the soup
            directions = [(index, li.text) for index, li in
                          enumerate(soup.find('ul', class_='direction-list svelte-1dqq0pw').find_all('li'))]

            return Recipe(title, img_src, ready_in, ingredient_num, ingredients, directions)
        else:
            return None

    def run(self, start_page, end_page):
        # iterate through the specified page range and fetch the result
        for page_number in range(start_page, end_page + 1):
            search_results = self.fetch_search_results(page_number)
            if search_results:
                subpage_urls = self.parse_search_results(search_results)
                for subpage_url in subpage_urls:
                    html_content = self.fetch_subpage(subpage_url)
                    if html_content:
                        subpage_info = self.parse_subpage(html_content)
                        file_storage = FileStorage()
                        file_storage.save_object(subpage_info, FILE_NAME)
                        print(f"Subpage information from {subpage_url}: {subpage_info}")

                        time.sleep(random.uniform(1, 3)) # sleep a random interval between 1 and 3 seconds to prevent website limitation
