from pyspark.sql import SparkSession

def word_count(input_file, output_file, gcs_bucket, gcs_output_path):
    spark = SparkSession.builder.appName("WordCount").getOrCreate()

    try:
        lines = spark.read.text(input_file).rdd.map(lambda r: r[0])

        words = lines.flatMap(lambda line: line.split(" "))

        word_key_values = words.map(lambda word: (word, 1))

        word_counts = word_key_values.reduceByKey(lambda a, b: a + b)

        result = word_counts.collect()

        result.saveAsTextFile("file://" + output_file)

        result.write.text("gs://{}/{}".format(gcs_bucket, gcs_output_path))

    finally:
        spark.stop()

if __name__ == "__main__":
    input_file = "./reut2-009.sgm" 
    output_file = "./result.txt" 
    gcs_bucket = "assignment2-prob1b"
    gcs_output_path = "./" 

    word_count(input_file, output_file, gcs_bucket, gcs_output_path)
