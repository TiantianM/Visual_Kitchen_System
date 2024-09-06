import requests
from bs4 import BeautifulSoup
import time
import random
import configparser

from file_storage import FileStorage
from ingredient import Ingredient

config = configparser.ConfigParser()
config.read('./config.ini')

FILE_NAME = config.get('Settings', 'FILE_NAME')

class IngredientScraper:
    def __init__(self, base_url):
        # Set up the base URL and headers for web scraping.
        self.base_url = base_url
        self.headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3'
        }

    def fetch_page(self, page_number):
        # Fetch a single page of ingredients.
        url = f"{self.base_url}?page={page_number}"
        try:
            response = requests.get(url, headers=self.headers)
            if response.status_code == 200:
                return response.content
            else:
                print(f"Failed to fetch page {page_number}: {response.status_code}")
                return None
        except Exception as e:
            print(f"An error occurred while fetching page {page_number}: {e}")
            return None

    def parse_page(self, html_content):
        # Parse the HTML content of a page to extract ingredients.
        if html_content:
            soup = BeautifulSoup(html_content, 'html.parser')
            table = soup.find('table', {'class': 'table-foods table-standard table-condensed'})
            tr_tags = table.tbody.find_all('tr')

            for tr in tr_tags:
                td_tags = tr.find_all('td')

                # Extract ingredient details.
                name = td_tags[1].a.text.strip() if td_tags[1].a else ''
                scientific_name = td_tags[2].text.strip() if td_tags[2] else ''
                food_group = td_tags[3].text.strip() if td_tags[3] else ''
                food_subgroup = td_tags[4].text.strip() if td_tags[4] else ''
                img = "https://foodb.ca/" + td_tags[5].a.img['src'] if td_tags[5] and td_tags[5].a and td_tags[5].a.img else ''

                # Create an Ingredient object and save it.
                ingredient = Ingredient(name, scientific_name, food_group, food_subgroup, img)
                storage = FileStorage()
                storage.save_object(ingredient, FILE_NAME)
                print(f"Ingredient: {ingredient}")

    def run(self, start_page, end_page):
        # Main method to run the scraper for a range of pages.
        for page_number in range(start_page, end_page + 1):
            html_content = self.fetch_page(page_number)
            if html_content:
                self.parse_page(html_content)

            # Random delay to prevent being blocked by the server.
            delay = random.uniform(1, 3)
            time.sleep(delay)
