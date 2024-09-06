import os
import pickle

class FileStorage:
    def __init__(self):
        # Set up a directory to store files.
        self.storage_path = os.path.join(os.getcwd(), 'data')
        if not os.path.exists(self.storage_path):
            os.makedirs(self.storage_path)

    def save_object(self, obj, filename):
        # Save an object to a file.
        file_path = os.path.join(self.storage_path, filename)
        # Determine if new file is created or appended.
        mode = 'wb' if not os.path.exists(file_path) else 'ab'
        with open(file_path, mode) as file:
            pickle.dump(obj, file)
        # Confirmation message.
        print(f"Object saved as {filename} in {self.storage_path}")

    def load_object(self, filename):
        # Load objects from a file.
        file_path = os.path.join(self.storage_path, filename)
        if os.path.exists(file_path):
            loaded_objects = []
            with open(file_path, 'rb') as file:
                # Read all objects from the file.
                while True:
                    try:
                        obj = pickle.load(file)
                        loaded_objects.append(obj)
                    except EOFError:
                        break
            # Confirmation message.
            print(f"All objects loaded from {filename}")
            return loaded_objects
        else:
            # File not found message.
            print(f"File {filename} does not exist.")
            return []
