import os
import pickle


class FileStorage:
    def __init__(self):
        # initialize the the path to save the result
        self.storage_path = os.path.join(os.getcwd(), 'data')
        if not os.path.exists(self.storage_path):
            os.makedirs(self.storage_path)

    def save_object(self, obj, filename):
        # save the result by dumping into the pickle
        file_path = os.path.join(self.storage_path, filename)
        mode = 'wb' if not os.path.exists(file_path) else 'ab'
        with open(file_path, mode) as file:
            pickle.dump(obj, file)
        print(f"Object saved as {filename} in {self.storage_path}")

    def load_object(self, filename):
        # (for later) load the pickle object back into memory
        file_path = os.path.join(self.storage_path, filename)
        if os.path.exists(file_path):
            loaded_objects = []
            with open(file_path, 'rb') as file:
                while True:
                    try:
                        obj = pickle.load(file)
                        loaded_objects.append(obj)
                    except EOFError:
                        break
            print(f"All objects loaded from {filename}")
            return loaded_objects
        else:
            print(f"File {filename} does not exist.")
            return []
