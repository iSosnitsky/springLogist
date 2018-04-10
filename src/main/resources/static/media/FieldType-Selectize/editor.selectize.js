/**
 * [Selectize](http://brianreavis.github.io/selectize.js/) enhances the HTML
 * `<select>` element with a beautifully styled input control, that features
 * tags, text input, auto-complete and much more.
 *
 * @name Selectize
 * @summary Use the Selectize library with Editor for complex select input options.
 * @requires [Selectize](http://brianreavis.github.io/selectize.js/)
 * @depcss //cdnjs.cloudflare.com/ajax/libs/selectize.js/0.9.0/css/selectize.css
 * @depjs //cdnjs.cloudflare.com/ajax/libs/selectize.js/0.9.0/js/standalone/selectize.js
 *
 * @opt `e-type object` **`options`**: Options that are given to the selectize
 *     `addOption` method.
 * @opt `e-type object` **`opts`**: Selectize initialisation options object.
 *     Please refer to the Selectize documentation for the full range
 *     of options available.
 * @opt `e-type object` **`attrs`**: Attributes that are applied to the
 *     `-tag select` element before selectize is initialised
 *
 * @method **`inst`**: Get the selectize instance
 * @method **`update`**: Clear existing options and add new items
 *
 * @scss editor.selectize.scss
 *
 * @example
 *   new $.fn.dataTable.Editor( {
 *   "ajax": "php/tableFormatting.php",
 *   "table": "#example",
 *   "fields": [ {
 *           "label": "Item:",
 *           "name": "item"
 *       }, {
 *           "label": "Priority:",
 *           "name": "priority",
 *           "type": "selectize",<div>
 *           "options": [
 *               { "label": "1 (highest)", "value": "1" },
 *               { "label": "2",           "value": "2" },
 *               { "label": "3",           "value": "3" },
 *               { "label": "4",           "value": "4" },
 *               { "label": "5 (lowest)",  "value": "5" }
 *           ]
 *       }, {
 *           "label": "Status:",
 *           "name": "status",
 *           "type": "radio",
 *           "default": "Done",
 *           "options": [
 *               { "label": "To do", "value": "To do" },
 *               { "label": "Done", "value": "Done" }
 *           ]
 *       }
 *   ]
 * } );
 */

(function ($, DataTable) {


    if (!DataTable.ext.editorFields) {
        DataTable.ext.editorFields = {};
    }

    var Editor = DataTable.Editor;
    var _fieldTypes = DataTable.ext.editorFields;


    _fieldTypes.selectize = {
        _addOptions: function (conf, options) {
            var selectize = conf._selectize;

            selectize.clearOptions();
            selectize.addOption(options);
            selectize.refreshOptions();
        },

        create: function (conf) {
            var container = $('<div/>');
            conf._input = $('<select/>')
                .attr($.extend({
                    id: conf.id
                }, conf.attr || {}))
                .appendTo(container);

            conf._input.selectize($.extend({
                valueField: 'value',
                labelField: 'label',
                dropdownParent: 'body'
            }, conf.opts));

            conf._selectize = conf._input[0].selectize;

            if (conf.options || conf.ipOpts) {
                _fieldTypes.selectize._addOptions(conf, conf.options || conf.ipOpts);
            }

            return container[0];
        },

        get: function (conf) {
            return conf._selectize.getValue();
        },

        set: function (conf, val) {
            return conf._selectize.setValue(val);
        },

        enable: function (conf) {
            conf._selectize.enable();
            $(conf._input).removeClass('disabled');
        },

        disable: function (conf) {
            conf._selectize.disable();
            $(conf._input).addClass('disabled');
        },

        // Non-standard Editor methods - custom to this plug-in
        inst: function (conf) {
            return conf._selectize;
        },

        update: function (conf, options) {
            _fieldTypes.selectize._addOptions(conf, options);
        }
    };


})(jQuery, jQuery.fn.dataTable);
