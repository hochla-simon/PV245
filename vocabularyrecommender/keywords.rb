#!/usr/bin/env ruby

# require 'rake_text'
require 'rubygems'
require 'highscore'
# require 'bloomfilter-rb'
# require 'stemmer'
require 'json'
# require 'active_support/inflector'
require "cld"
require "i18n"


@blacklistCZ  = Highscore::Blacklist.load_file "stopwords/stopwords_cz.txt"
@blacklistENG = Highscore::Blacklist.load_file "stopwords/stopwords_en.txt"
@blacklistSK  = Highscore::Blacklist.load_file "stopwords/stopwords_sk.txt"
#blacklistUniversal = Highscore::Blacklist.load_file "stopwords/stopwords.txt"

def whatLanguage txt 
	if txt.include? 'sa'
		:slovak
	elsif txt.include? 'se'
		:czech 
	elsif txt.include? 'and'
		:english 
	else
		:english
	end
end

def what_blacklist(text)
	case CLD.detect_language(text)[:code]
	when "cz"
		return @blacklistCZ
	when "sk"
		return @blacklistSK
	else
		return @blacklistENG
	end
end

def integer?(str)
  /[+-]?\d+/ === str
end

def count_keywords(text, blacklist)
	keywords = text.keywords(blacklist) do
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
	return keywords
end

filename = 'inputExample.json'
filename = ARGV[1] if ARGV[1]

limit = 3
limit = ARGV[0].to_i if ARGV[0]

I18n.config.available_locales = :en

def get_keywords(filename, limit)
	file = File.read(filename)
	corpus = JSON.parse(file)

	array = []

	corpus.each do |text|
		
		#puts CLD.detect_language(text)[:code]

		blacklist = what_blacklist(text)
		#text = text.parameterize.underscore.humanize
		text = I18n.transliterate text.gsub(/[0-9!@%&.,?><\/}{()"#$\*]/,"")

		keywords = count_keywords(text, blacklist)

		output = Hash.new
		keywords.top(limit).each do |k|		
			#output[k.text] = k.weight.round(2) unless integer? k.text
			output[k.text] = CLD.detect_language(text)[:code]
		end
		array.push(output)
	end

	json_str = array.to_json
	File.open("keyword_output.json", 'w') { |file| file.write(json_str) }
	return json_str
end

puts get_keywords(filename, limit)
