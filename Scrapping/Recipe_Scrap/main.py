import configparser
from file_storage import FileStorage
from food_scraper import FoodScraper

config = configparser.ConfigParser()
config.read('./config.ini')

URL = config.get('Settings', 'URL')
START_PAGE = config.getint('Settings', 'START_PAGE')
END_PAGE = config.getint('Settings', 'END_PAGE')
FILE_NAME = config.get('Settings', 'FILE_NAME')


def do_crawler():
    search_url = URL
    start_page = START_PAGE
    end_page = END_PAGE
    scraper = FoodScraper(search_url)
    scraper.run(start_page, end_page)


def load_pkl():
    file_storage = FileStorage()
    loaded_objects = file_storage.load_object(FILE_NAME)
    for obj in loaded_objects:
        print(obj)
        print("\n")


if __name__ == "__main__":
    # do_crawler()
    load_pkl()
