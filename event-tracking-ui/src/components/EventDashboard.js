import React, { useState, useEffect } from 'react';
import EventTable from './EventTable';
import EventForm from './EventForm';
import { eventService } from '../services/eventService';
import '../styles/EventDashboard.css';

const EventDashboard = () => {
    const [events, setEvents] = useState([]);
    const [activeEvents, setActiveEvents] = useState([]);
    const [archivedEvents, setArchivedEvents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [showForm, setShowForm] = useState(false);
    const [editingEvent, setEditingEvent] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null);

    useEffect(() => {
        loadEvents();
    }, []);

    const loadEvents = async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await eventService.getAllEvents();
            setEvents(data);
            categorizeEvents(data);
        } catch (err) {
            setError('Failed to load events. Please try again.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const categorizeEvents = (allEvents) => {
        const now = new Date();
        const active = [];
        const archived = [];

        allEvents.forEach(event => {
            const endDate = new Date(event.endDate);
            if (endDate < now) {
                archived.push(event);
            } else {
                active.push(event);
            }
        });

        setActiveEvents(active.sort((a, b) => new Date(a.startDate) - new Date(b.startDate)));
        setArchivedEvents(archived.sort((a, b) => new Date(b.endDate) - new Date(a.endDate)));
    };

    const handleCreateOrUpdate = async (formData) => {
        try {
            setLoading(true);
            if (editingEvent) {
                await eventService.updateEvent(editingEvent.id, formData);
                setSuccessMessage('Event updated successfully!');
            } else {
                await eventService.createEvent(formData);
                setSuccessMessage('Event created successfully!');
            }
            setShowForm(false);
            setEditingEvent(null);
            loadEvents();
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to save event');
        } finally {
            setLoading(false);
        }
    };

    const handleEdit = (event) => {
        setEditingEvent(event);
        setShowForm(true);
    };

    const handleDelete = async (eventId) => {
        if (window.confirm('Are you sure you want to delete this event?')) {
            try {
                setLoading(true);
                await eventService.deleteEvent(eventId);
                setSuccessMessage('Event deleted successfully!');
                loadEvents();
            } catch (err) {
                setError('Failed to delete event');
            } finally {
                setLoading(false);
            }
        }
    };

    const handleCancel = () => {
        setShowForm(false);
        setEditingEvent(null);
    };

    const handleCloseMessage = () => {
        setSuccessMessage(null);
        setError(null);
    };

    return (
        <div className="event-dashboard">
            <header className="dashboard-header">
                <h1>📅 Event Tracking System</h1>
            </header>

            {successMessage && (
                <div className="message message-success">
                    {successMessage}
                    <button className="close-btn" onClick={handleCloseMessage}>×</button>
                </div>
            )}

            {error && (
                <div className="message message-error">
                    {error}
                    <button className="close-btn" onClick={handleCloseMessage}>×</button>
                </div>
            )}

            <div className="dashboard-content">
                {showForm ? (
                    <div className="form-section">
                        <h2>{editingEvent ? 'Edit Event' : 'Create New Event'}</h2>
                        <EventForm
                            initialData={editingEvent}
                            onSubmit={handleCreateOrUpdate}
                            onCancel={handleCancel}
                        />
                    </div>
                ) : (
                    <>
                        <div className="section">
                            <div className="section-header">
                                <h2>Active Events ({activeEvents.length})</h2>
                                <button
                                    className="btn btn-primary"
                                    onClick={() => setShowForm(true)}
                                    disabled={loading}
                                >
                                    + New Event
                                </button>
                            </div>
                            {loading ? (
                                <div className="loading">Loading events...</div>
                            ) : (
                                <EventTable
                                    events={activeEvents}
                                    onEdit={handleEdit}
                                    onDelete={handleDelete}
                                    isArchived={false}
                                />
                            )}
                        </div>

                        <div className="section">
                            <h2>Archived Events ({archivedEvents.length})</h2>
                            {loading ? (
                                <div className="loading">Loading events...</div>
                            ) : (
                                <EventTable
                                    events={archivedEvents}
                                    onEdit={handleEdit}
                                    onDelete={handleDelete}
                                    isArchived={true}
                                />
                            )}
                        </div>
                    </>
                )}
            </div>
        </div>
    );
};

export default EventDashboard;
