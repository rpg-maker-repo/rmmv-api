
(function(exports) {
"use strict";

	/**
	 * Handler used when no handler is passed to the
	 * JSDocParser constructor.
	 */
	var DefaultHandler = {

		handle: function(current)
		{
			current.meta = current.meta || {};

			current.meta[current.tag] = {
				type: current.type,
				ident: current.ident,
				text: current.text
			};
		}

	};

	/**
	 * JSDOC Parser
	 *
	 * @param {object} handler Optional handler object.
	 *
	 * The handler is an object like this:
	 *
	 * <pre><code>
	 * var handler = {
	 *
	 *     alias: function(tag)
	 *     {
	 *     // do something when it finds tag alias
	 *     },
	 *     // Handle all other tags
	 *     handle: function(tag)
	 *     {
	 *     }
	 * }
	 * </code></pre>
	 *
	 * The <code>tag</code> parameter will contain the following fields:
	 *
	 * <pre><code>
	 * tag = {
	 *     tag: null,
	 *     type: null,
	 *     ident: null,
	 *     text: null,
	 *     start: 0,
	 *     end: 0,
	 *     meta: null
	 * }
	 * </code></pre>
	 */
	function JSDocParser(handler)
	{
		this.handler = handler || DefaultHandler;
		// We will reuse this object to pass the match to the handler.
		this.current = {
			tag: null,
			type: null,
			ident: null,
			text: null,
			start: 0,
			end: 0
		};

		this.map = this._buildMap();
	}

	JSDocParser.prototype = {

		REGEX: /@(\w+)([ \t]*)(.*)$/gm,
		CLEAN: /^\s*[\/\*]+\s?|[ \t]+$/gm,

		TYPE: /^\{\(?([\w\d_ <>\|\.\#]+)\)?\}\s*|([\w\d_\.#]+)\s*/,
		TYPENAMETEXT: /^\{\(?([ <>\w\d_\|\.\#]+)\)?\}\s*([\.\w\d_]*)\s*(.*)/,
		TYPETEXT: /^(\{\(?([ <>\w\d_\|\.\#]+)\)?\})?\s*(.*)/,
		NAMETEXT: /([\.\w\d_]+)\s*(.*)/,

		alias: {
			virtual: 'abstract',
			augments: 'extends',
			extend: 'extends',
			'const': 'constant',
			'constructor': 'constructor',
			'emits': 'fires',
			'return': 'returns',
			'memberOf': 'memberof'
		},

		parsers: {
			// @tag {type}
			parseType: 'alias|borrows|callback|constant|constructs|const|enum|emits|event|type|exports|extends|external|fires|lends|memberof|method|mixes|mixin|name',
			// @tag [{type} name text]
			parseTypeNameText: 'param|property|typedef',
			// @tag [{type} text]
			parseTypeText: 'returns|this',
			// @tag [text]
			parseText: 'author|deprecated|copyright|classdesc|desc|license|requires|since|summary|throws|todo|variation|version|see'

			/*
			Other tags that don't require parsing:
			case 'abstract': case 'global': case 'protected': case 'public':
			case 'private': case 'readonly': case 'virtual': case 'inner':
			case 'instance': case 'license': case 'static':
			*/
		},

		/** @private
		 * Builds the parser map.
		 */
		_buildMap: function()
		{
			var map = {}, parser, tags, tag;

			for (parser in this.parsers)
			{
				tags = this.parsers[parser].split('|');
				while ((tag = tags.pop()))
					map[tag] = this[parser];
			}

			map.constructor = false;

			return map;
		},

		/** @private */
		parseNameText: function(current, text)
		{
			var m = this.NAMETEXT.exec(text);
			if (m)
			{
				current.ident = m[1];
				current.text = m[2];
			}

			return text.length;
		},

		/** @private */
		parseTypeNameText: function(current, text)
		{
			var m = this.TYPENAMETEXT.exec(text);

			if (m)
			{
				current.type = m[1];
				current.ident = m[2];
				current.text = m[3];

				return text.length;
			}

			return this.parseNameText(current, text);

		},

		/** @private */
		parseTypeText: function(current, text)
		{
			var m = this.TYPETEXT.exec(text);
			current.type = m[2];
			current.text = m[3];
			return text.length;
		},

		/** @private */
		parseType: function(current, text)
		{
			var m = this.TYPE.exec(text);

			if ((current.text = m && (m[1] || m[2])))
				current.type = current.text
				;

			return m && m[0].length;
		},

		/** @private */
		parseText: function(current, text)
		{
			current.text = text;
			return text.length;
		},

		/** @private */
		parseMatch: function(match)
		{
		var
			current = this.current,
			tag = current.tag = this.alias[match[1]] || match[1],
			l = 0,
			parser = this.map[tag]
		;
			current.start = match.index;
			current.type = current.ident = current.text = null;

			l = (parser && parser.call(this, current, match[3])) || l;

			current.end = current.start +
				match[1].length + 1 + match[2].length + l;

			return current;
		},

		/** @private */
		parseRegex: function(comment, regex)
		{
			var match, current, tag;

			while ((match = regex.exec(comment)))
			{
				current = this.parseMatch(match);
				tag = current.tag;
				// Call handler function
				(this.handler[tag] || this.handler.handle).call(this.handler, current);

				comment = comment.slice(0, current.start) +
					comment.slice(current.end);
				regex.lastIndex = current.start;
			}

			return comment;
		},

		/**
		 * @param {string} comment The comment to parse
		 * @param meta An optional object to pass to the handler.
		 */
		parse: function(comment, meta)
		{
			if ((comment[0] !== '*' && comment[0] !== '/') ||
				/@ignore(?=\s|$)/.test(comment))
				return;

			this.current.meta = meta;

			comment = this.parseRegex(
				comment.replace(this.CLEAN, ''), this.REGEX
			).trim();

			if (comment)
			{
				this.current.tag = 'desc';
				this.current.text = comment;
				(this.handler.desc || this.handler.handle)(this.current);
			}

			return this.current.meta;
		}

	};

	exports.JSDocParser= JSDocParser;

})(typeof(exports)==='undefined' ? (this.j5g3 || (this.j5g3={})) : exports);
