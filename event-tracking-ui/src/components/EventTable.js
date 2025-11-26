import React, { useState } from 'react';
import '../styles/EventTable.css';

const EventTable = ({ events, onEdit, onDelete, isArchived = false }) => {
    const [filterText, setFilterText] = useState('');

    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        const options = { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' };
        return new Date(dateString).toLocaleDateString('en-US', options);
    };

    const filteredEvents = events.filter(event =>
        event.name.toLowerCase().includes(filterText.toLowerCase()) ||
        event.description.toLowerCase().includes(filterText.toLowerCase()) ||
        event.location.toLowerCase().includes(filterText.toLowerCase())
    );

    return (
        <div className="event-table-container">
            <div className="table-filter">
                <input
                    type="text"
                    placeholder="Search by name, description, or location..."
                    value={filterText}
                    onChange={(e) => setFilterText(e.target.value)}
                    className="filter-input"
                />
                {filterText && (
                    <button
                        className="btn-clear-filter"
                        onClick={() => setFilterText('')}
                        title="Clear filter"
                    >
                        ✕
                    </button>
                )}
            </div>
            <table className="event-table">
                <thead>
                    <tr>
                        <th>Event Name</th>
                        <th>Description</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Location</th>
                        <th>Attendees</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredEvents.length === 0 ? (
                        <tr>
                            <td colSpan="7" className="no-data">
                                No {isArchived ? 'archived' : 'active'} events found
                            </td>
                        </tr>
                    ) : (
                        filteredEvents.map(event => (
                            <tr key={event.id}>
                                <td className="event-name">{event.name}</td>
                                <td className="event-description">{event.description}</td>
                                <td>{formatDate(event.startDate)}</td>
                                <td>{formatDate(event.endDate)}</td>
                                <td>{event.location}</td>
                                <td className="attendees">
                                    {event.minAttendees || 0} - {event.maxAttendees || '∞'}
                                </td>
                                <td className="actions">
                                    {!isArchived && (
                                        <>
                                            <button
                                                className="btn btn-sm btn-edit"
                                                onClick={() => onEdit(event)}
                                                title="Edit event"
                                            >
                                                ✏️ Edit
                                            </button>
                                            <button
                                                className="btn btn-sm btn-delete"
                                                onClick={() => onDelete(event.id)}
                                                title="Delete event"
                                            >
                                                🗑️ Delete
                                            </button>
                                        </>
                                    )}
                                    {isArchived && (
                                        <span className="badge badge-archived">Archived</span>
                                    )}
                                </td>
                            </tr>
                        ))
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default EventTable;
