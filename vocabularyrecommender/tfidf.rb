#!/usr/bin/env ruby

#Potrebne mat nainstalovane ruby, gem a sputit prikazy:
# gem install bundler
# bundle install 

#require 'rubygems'
require 'ruby-tf-idf'
require 'json'

filename = 'input.json'
filename = ARGV[1] if ARGV[1]

limit = 1 #restrict to the top 3 relevant words per document
limit = ARGV[0].to_i if ARGV[0]


file = File.read(filename)
corpus = JSON.parse(file)

exclude_stop_words = true

@t = RubyTfIdf::TfIdf.new(corpus, limit, exclude_stop_words)
output =  @t.tf_idf
#puts output

json_str = output.to_json
File.open("tfidf_output.json", 'w') { |file| file.write(json_str) }
puts json_str
