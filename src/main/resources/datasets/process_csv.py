from add_headers import add_headers
import os

def process_all_csv_files():
    # Get the list of all files in the current directory
    files = os.listdir()

    # Filter out only the CSV files
    csv_files = [file for file in files if file.endswith('.csv')]
    print(csv_files)

    # Process each CSV file
    for csv_file in csv_files:
        print(csv_file)
        add_headers(csv_file)

# Call the function to process all CSV files
process_all_csv_files()
