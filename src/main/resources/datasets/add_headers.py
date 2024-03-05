import csv
import os

def add_headers(csv_file):
    # Read the CSV file
    with open(csv_file, 'r') as file:
        reader = csv.reader(file)
        rows = list(reader)

    # Infer column names based on data structure
    headers = ['Time', 'Value']  # Default headers

    # Update headers based on data analysis
    if len(rows) > 0:
        if len(rows[0]) >= 2:
            headers[1] = 'Temperature'  # Assuming the second column is temperature

    # Add headers to the data
    rows.insert(0, headers)

    # Create a new directory if it doesn't exist
    directory = 'processed_datasets'
    if not os.path.exists(directory):
        os.makedirs(directory)

    # Write the modified data to a new CSV file in the processed_datasets folder
    new_file = os.path.join(directory, os.path.basename(csv_file))
    with open(new_file, 'w', newline='') as file:
        writer = csv.writer(file)
        writer.writerows(rows)

    print(f"Headers added to {csv_file}. New file saved as {new_file}")

# Example usage
# add_headers('log-2ET-221.csv')
# add_headers('log-4ET-411.csv')

