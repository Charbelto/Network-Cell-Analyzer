import time

from flask import Flask, current_app, Blueprint, flash, request, jsonify, abort, g, render_template
from datetime import datetime, timedelta
from collections import defaultdict
from flask_cors import CORS
import sqlite3
import os

app = Flask(__name__, instance_relative_config=True)


@app.route('/')
def hello_world():
    return 'Hello World!'


CORS(app)
bp = Blueprint("app", __name__)
instance_folder = app.instance_path
if not os.path.exists(instance_folder):
    # Create the instance folder if it doesn't exist
    os.makedirs(instance_folder)
app.config.from_mapping(
    # a default secret that should be overridden by instance config
    SECRET_KEY="dev",
    # store the database in the instance folder
    DATABASE=os.path.join(app.instance_path, "mydb.sqlite"),
)


# Routes for adding network info and retrieving network info


@app.route("/add_network_info", methods=["POST"])
def create_net_info():
    req_json = request.get_json()
    error_message = validate_net_info(req_json)
    if error_message:
        flash(error_message)
        return jsonify({"error": error_message}), 400
    else:
        save_net_info(req_json)
        return jsonify({"message": "Network info saved successfully"}), 201


# Validation and saving functions


def validate_net_info(req_json):
    required_fields = ['operator', 'signal_strength', 'SNR', 'network_type', 'frequency_band', 'cell_id',
                       'timestamp', 'mac_address', 'ip_address']
    for field in required_fields:
        if field not in req_json or not req_json[field]:
            return f"{field.replace('_', ' ').title()} is required."
    return None


def save_net_info(req_json):
    db = g.get('db')
    if db is None:
        db = g.db = sqlite3.connect(
            current_app.config['DATABASE'],
            detect_types=sqlite3.PARSE_DECLTYPES
        )
        g.db.row_factory = sqlite3.Row
    db.execute(
        "INSERT INTO cell_info (operator, signal_strength, SNR, network_type, frequency_band, cell_id, timestamp, mac_address, ip_address, created) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        (req_json['operator'], req_json['signal_strength'], req_json['SNR'], req_json['network_type'],
         req_json['frequency_band'], req_json['cell_id'], req_json['timestamp'], req_json['mac_address'],
         req_json['ip_address'], datetime.now())
    )
    db.commit()


# Routes for retrieving network info


@app.route("/network_info", methods=["GET"])
def get_network_info():
    db = g.get('db')
    if db is None:
        db = g.db = sqlite3.connect(
            current_app.config['DATABASE'],
            detect_types=sqlite3.PARSE_DECLTYPES
        )
        db.row_factory = sqlite3.Row
    net_infs = db.execute(
        "SELECT * FROM cell_info ORDER BY created DESC"
    ).fetchall()
    return jsonify(format_net_info(net_infs))


def format_net_info(net_infs):
    formatted_data = []
    for row in net_infs:
        formatted_data.append({
            'operator': row['operator'],
            'signal_strength': row['signal_strength'],
            'SNR': row['SNR'],
            'network_type': row['network_type'],
            'frequency_band': row['frequency_band'],
            'cell_id': row['cell_id'],
            'timestamp': row['timestamp'],
            'mac_address': row['mac_address'],
            'ip_address': row['ip_address'],
            'created': row['created']
        })
    return formatted_data


# Route for retrieving connected devices count


@app.route("/connected_devices", methods=["GET"])
def get_connected_devices():
    connected_devices_count = count_connected_devices()
    return connected_devices_count


@app.route("/server_interface")
def render_server_interface():
    db = g.get('db')
    if db is None:
        db = g.db = sqlite3.connect(
            current_app.config['DATABASE'],
            detect_types=sqlite3.PARSE_DECLTYPES
        )
        db.row_factory = sqlite3.Row
    IP_MAC_DISTINCT =  db.execute("SELECT DISTINCT mac_address, ip_address FROM cell_info ORDER BY created DESC").fetchall()
    count_connected_device = count_connected_devices()
    connected_devices_count = count_connected_device[1]
    currently_connected_devices = count_connected_device[0]
    return render_template("home/page.html", connected_devices_count=connected_devices_count, IP_MAC_DISTINCT=IP_MAC_DISTINCT, currently_connected_devices=currently_connected_devices)


def count_connected_devices():
    db = g.get('db')
    if db is None:
        db = g.db = sqlite3.connect(
            current_app.config['DATABASE'],
            detect_types=sqlite3.PARSE_DECLTYPES
        )
        db.row_factory = sqlite3.Row
    five_seconds_ago = datetime.now() - timedelta(seconds=5)
    net_infs = db.execute(
        "SELECT DISTINCT ip_address FROM cell_info WHERE created >= ?",
        (five_seconds_ago,)
    ).fetchall()
    return net_infs, len(net_infs)


# Route for retrieving device list


@app.route("/device_list", methods=["GET"])
def get_device_list():
    device_list = fetch_device_list()
    return jsonify({"device_list": device_list})


def fetch_device_list():
    db = g.get('db')
    if db is None:
        db = g.db = sqlite3.connect(
            current_app.config['DATABASE'],
            detect_types=sqlite3.PARSE_DECLTYPES
        )
        db.row_factory = sqlite3.Row
    net_infs = db.execute(
        "SELECT DISTINCT mac_address, ip_address FROM cell_info ORDER BY created DESC"
    ).fetchall()
    device_list = [{"mac": row['mac_address'], "ip": row['ip_address']} for row in net_infs]
    return device_list


# Route for retrieving information of a specific device


@app.route("/device/<string:mac_address>", methods=["GET"])
def get_device_info(mac_address):
    device_info = fetch_device_info(mac_address)
    if device_info:
        return jsonify(device_info)
    else:
        abort(404)


def fetch_device_info(mac_address):
    db = g.get('db')
    if db is None:
        db = g.db = sqlite3.connect(
            current_app.config['DATABASE'],
            detect_types=sqlite3.PARSE_DECLTYPES
        )
        db.row_factory = sqlite3.Row
    device_info = db.execute(
        "SELECT * FROM cell_info WHERE mac_address = ? ORDER BY created DESC",
        mac_address
    ).fetchone()
    if device_info:
        return {
            'operator': device_info['operator'],
            'signal_strength': device_info['signal_strength'],
            'SNR': device_info['SNR'],
            'network_type': device_info['network_type'],
            'frequency_band': device_info['frequency_band'],
            'cell_id': device_info['cell_id'],
            'timestamp': device_info['timestamp'],
            'mac_address': device_info['mac_address'],
            'ip_address': device_info['ip_address'],
            'created': device_info['created']
        }
    else:
        return None


@app.route("/hello")
def hello():
    return "Hello!"


# Route for retrieving network statistics


@app.route("/get_statistics", methods=["POST"])
def get_network_statistics():
    req_json = request.get_json()
    start_datetime = req_json['start_datetime']
    end_datetime = req_json['end_datetime']
    if not start_datetime or not end_datetime:
        return jsonify({"error": "Please provide both start_datetime and end_datetime parameters"}), 400
    else:
        infs = fetch_statistics_from_db(start_datetime, end_datetime)
        print(infs)
        results = calculate_net_statistics(infs)
        return jsonify(results)


def fetch_statistics_from_db(start_datetime, end_datetime):
    print(start_datetime)
    print(end_datetime)
    db = g.get('db')
    if db is None:
        db = g.db = sqlite3.connect(
            current_app.config['DATABASE'],
            detect_types=sqlite3.PARSE_DECLTYPES
        )
        db.row_factory = sqlite3.Row
    sql = 'SELECT * FROM cell_info WHERE created BETWEEN ' +"'"+ start_datetime +"'"+ ' AND ' +"'"+ end_datetime +"'"+ ' ORDER BY created DESC'
    print(sql, (start_datetime, end_datetime))
    return db.execute(
        sql
    ).fetchall()


def calculate_net_statistics(infs):
    operator_stats = defaultdict(int)
    network_type_stats = defaultdict(int)
    signal_strength_stats = defaultdict(int)
    snr_stats = defaultdict(int)
    device_stats = defaultdict(int)
    device_power_stats = defaultdict(int)
    total_records = 0

    for row in infs:
        operator = row['operator']
        network_type = row['network_type']
        signal_strength = int(row['signal_strength'])
        if row['snr'] != "N/A":
            if int(row['snr']) < 300:
                snr = int(row['SNR'])
                snr_stats[network_type] += snr
        device = row['ip_address']

        operator_stats[operator] += 1
        network_type_stats[network_type] += 1
        signal_strength_stats[network_type] += signal_strength

        device_stats[device] += 1
        device_power_stats[device] += signal_strength
        total_records += 1

    # Calculate operator and network type percentages
    operator_percentage = {operator: str(round((count / total_records) * 100, 4))+"%" for operator, count in operator_stats.items()}
    network_type_percentage = {net_type: str(round((count / total_records) * 100, 4))+"%" for net_type, count in
                               network_type_stats.items()}

    # Calculate average power and SNR per network type
    average_power_per_network = {net_type: str(round(signal_strength_stats[net_type] / network_type_stats[net_type], 4))+" dbm" for
                                 net_type in signal_strength_stats}
    average_snr_per_network = {net_type: str(round(snr_stats[net_type] / network_type_stats[net_type], 4))+" dbm" for net_type
                               in snr_stats}

    # Calculate average power per device
    average_power_per_device = {device: str(round(device_power_stats[device] / device_stats[device], 4)) + " dbm" for device in
                                device_power_stats}

    return {
        'operator': operator_percentage,
        'net_type': network_type_percentage,
        'average_power': average_power_per_network,
        'average_snr_per_network': average_snr_per_network,
        'average_power_per_device': average_power_per_device
    }


if __name__ == "__main__":
    app.run(host="0.0.0.0")
