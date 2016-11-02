#!/usr/bin/env ruby

require 'rake_text'
require 'highscore'
require 'bloomfilter-rb'
require 'stemmer'
require 'json'

filename = 'input.json'
filename = ARGV[1] if ARGV[1]

limit = 2
limit = ARGV[0].to_i if ARGV[0]

file = File.read(filename)
corpus = JSON.parse(file)

array = []

blacklistCZ  = Highscore::Blacklist.load_file "stopwords/stopwords_cz.txt"
blacklistENG = Highscore::Blacklist.load_file "stopwords/stopwords_en.txt"
blacklistSK  = Highscore::Blacklist.load_file "stopwords/stopwords_sk.txt"
#blacklist = blacklist = Highscore::Blacklist.load_file "stopwords.txt"

corpus.each do |text|
	keywords = text.keywords(blacklistENG) do
	  set :multiplier, 10
	  set :upper_case, 3
	  set :long_words, 2
	  set :long_words_threshold, 15
	  set :short_words_threshold, 3     # => default: 2
	  set :bonus_multiplier, 2           # => default: 3
	  set :vowels, 1                     # => default: 0 = not considered
	  set :consonants, 5                 # => default: 0 = not considered
	  set :ignore_case, true             # => default: false
	  set :word_pattern, /[\w]+[^\s0-9]/ # => default: /\w+/
	  set :stemming, true                # => default: false
	end

	output = Hash.new
	keywords.top(limit).each do |k|
		output[k.text] = k.weight
	end
	array.push(output)
end

json_str = array.to_json
File.open("keyword_output.json", 'w') { |file| file.write(json_str) }

puts json_str
