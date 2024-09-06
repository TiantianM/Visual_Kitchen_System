import configparser
from file_storage import FileStorage
from ingredient_scraper import IngredientScraper

config = configparser.ConfigParser()
config.read('./config.ini')  # Load configuration from file.

URL = config.get('Settings', 'URL')  # Base URL for scraping.
START_PAGE = config.getint('Settings', 'START_PAGE')  # Start page number.
END_PAGE = config.getint('Settings', 'END_PAGE')  # End page number.
FILE_NAME = config.get('Settings', 'FILE_NAME')  # File name for saving/loading data.

def do_crawler():
    # Set up and run the scraper.
    search_url = URL
    start_page = START_PAGE
    end_page = END_PAGE
    scraper = IngredientScraper(search_url)
    scraper.run(start_page, end_page)

def load_pkl():
    # Load and print objects from the pickle file.
    file_storage = FileStorage()
    loaded_objects = file_storage.load_object(FILE_NAME)
    for obj in loaded_objects:
        print(obj)
        print("\n")

if __name__ == "__main__":
    # Main entry point: choose to either crawl or load data.
    # do_crawler()  # Uncomment to run the crawler.
    load_pkl()  # Load and print data from file.
